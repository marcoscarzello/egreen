package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.FirebaseStorage


class RegisterFragment : Fragment(R.layout.fragment_register) {

    //forse da mettere in viemodel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //HIDE bottom Bar
        val activity = activity as MainActivity?
        activity?.hideBottomBar(true)

        val db = Firebase.database.reference
        val ref = db.child("users") // primo ramo del database sotto cui ci saranno tutti i vari utenti (ciascuno con userID unico)

        val viewModel: MainViewModel by activityViewModels()

        var r_username = view.findViewById<EditText>(R.id.username)
        var r_email = view.findViewById<EditText>(R.id.email)
        var r_password = view.findViewById<EditText>(R.id.password)
        var r_confirmPassword = view.findViewById<EditText>(R.id.confirm_password)
        var registerButton = view.findViewById<Button>(R.id.registerButton)
        var googlebtn = view.findViewById<Button>(R.id.googlebtn2)

        googlebtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }



        val login = view.findViewById<TextView>(R.id.login)

        //registered already? go to login activity!
        login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            when {
                TextUtils.isEmpty(r_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(r_username.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "Please enter username.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(r_password.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(r_confirmPassword.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "Please confirm your password.",     //questo non compare mai bo perchè è anche vera la successiva
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //caso password!=confirm_password
                !r_confirmPassword.text.toString().contentEquals(r_password.text.toString()) ->{
                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "Your password doesn't match with confirm password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                else -> {

                    val email: String = r_email.text.toString().trim { it <= ' ' }
                    val password: String = r_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->


                                //if registration succesfully done
                                if (task.isSuccessful) {

                                    //Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    val currentUserDb = ref.child((firebaseUser.uid))    //sottoramo di users che ha come chiave l'userID assegnato al nuovo utente
                                    currentUserDb.child("username")?.setValue(r_username.text.toString())        //metto nel ramo dell'utente creato lo username
                                    currentUserDb.child("email")?.setValue(r_email.text.toString())              //metto nel ramo dell'utente creato la mail
                                    currentUserDb.child("plants")?.setValue("")       //metto nel ramo dell'utente creato la mail

                                    Toast.makeText(
                                        this@RegisterFragment.requireContext(),
                                        "You are registered succesfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

/*
                                    FirebaseStorage.getInstance().reference.child("Users").child(firebaseUser.uid).putFile(
                                        Uri.fromFile())

 */

                                    viewModel.updateCurrentUser()
                                    activity?.hideBottomBar(false)
                                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                                } else {
                                    //if registering is not successful then show error message
                                    Toast.makeText(
                                        this@RegisterFragment.requireContext(),
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }

    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "FirebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val activity = activity as MainActivity?
        val viewModel: MainViewModel by activityViewModels()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->

                if(task.isSuccessful){
                    Log.d(ContentValues.TAG, "signInWithCredential::Success")
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    Toast.makeText(
                        this@RegisterFragment.requireContext(),
                        "You are logged in succesfully",
                        Toast.LENGTH_SHORT
                    ).show()


                    viewModel.updateCurrentUser()
                    viewModel.getUsername()

                    activity?.hideBottomBar(false)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }else{
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)

                }
            }

    }

    companion object{
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA_NAME"
    }
}