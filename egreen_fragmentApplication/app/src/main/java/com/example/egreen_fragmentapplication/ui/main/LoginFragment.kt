package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment(R.layout.fragment_login) {

//forse da mettere in viemodel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //HIDE bottom Bar
        val activity = activity as MainActivity?
        activity?.hideBottomBar(true)

        val viewModel: MainViewModel by activityViewModels()

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val l_email = view.findViewById<TextView>(R.id.login_email)
        val l_password = view.findViewById<TextView>(R.id.login_psw)
        val register = view.findViewById<TextView>(R.id.register)
        val googleButton = view.findViewById<Button>(R.id.googlebtn)

        val email  = view.findViewById<TextView>(R.id.mail)
        viewModel.currentuser.observe(this, Observer { u -> email.text = u?.email })
        //not registered yet? go to register activity!
        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("500292871531-l6n061cpjo6srokcd8eh6dodr5n1j735.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@LoginFragment.requireActivity(), gso)

        googleButton.setOnClickListener {
            signIn()
        }

        loginButton.setOnClickListener {
            when {
                TextUtils.isEmpty(l_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginFragment.requireContext(),
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                TextUtils.isEmpty(l_password.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginFragment.requireContext(),
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    val email: String = l_email.text.toString().trim { it <= ' ' }
                    val password: String = l_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->


                                //if login succesfully done
                                if (task.isSuccessful) {

                                    //Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@LoginFragment.requireContext(),
                                        "You are logged in succesfully",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                    viewModel.updateCurrentUser()
                                    viewModel.getUsername()
                                    //viewModel.getPlants() //carica piante
                                    //Log.d("Quali piante sono caricate: ", viewModel.plantList.value.toString())
                                    //viewModel.getWtValues()
                                    //viewModel.getHmValues()
                                    activity?.hideBottomBar(false)
                                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                    //findNavController().navigate(R.id.action_accountSettingsFragment_to_gardenSettingsFragment)
                                } else {
                                    //if logging in is not successful then show error message
                                    Toast.makeText(
                                        this@LoginFragment.requireContext(),
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
                        this@LoginFragment.requireContext(),
                        "You are logged in succesfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    //qua devo fare il controllo per capire se è la prima  volta che hlaccede (isnewuser) o meno. Se no se fa ogni volta i comandi seguenti si perdono tutti i dati ogni volta
                    Log.e(TAG, firebaseUser.uid.toString())
                    val currentUserDb = Firebase.database.reference.child("users").child((firebaseUser.uid))    //sottoramo di users che ha come chiave l'userID assegnato al nuovo utente
                    currentUserDb.child("email")?.setValue(firebaseUser.email)              //metto nel ramo dell'utente creato la mail


                    currentUserDb.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.child("username").exists()) {
                                currentUserDb.child("username")?.setValue(firebaseUser.email)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })

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
