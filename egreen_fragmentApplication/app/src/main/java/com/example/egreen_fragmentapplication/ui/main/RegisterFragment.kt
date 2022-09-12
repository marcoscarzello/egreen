package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment(R.layout.fragment_register) {

    //forse da mettere in viemodel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val textInputLayouts: List<TextInputLayout> = listOf( r_user_text_input, r_email_text_input, r_psw_text_input, psw2_text_input)

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
            var noErrors = true
            for (textInputLayout in textInputLayouts) {
                val editTextString = textInputLayout.editText!!.text.toString()
                if (editTextString.isEmpty()) {
                    textInputLayout.error = resources.getString(R.string.error_string)
                    noErrors = false
                } else {
                    textInputLayout.error = null
                }
            }
            when(noErrors){
                //caso password!=confirm_password
                !r_confirmPassword.text.toString().contentEquals(r_password.text.toString()) ->{
                    psw2_text_input.error = "Password doesn't match"
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
                                    currentUserDb.child("darkMode")?.setValue(false)    //setto a false la dark mode del nuovo utente

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
                                    viewModel.getUsername()
                                    viewModel.getDarkMode()
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