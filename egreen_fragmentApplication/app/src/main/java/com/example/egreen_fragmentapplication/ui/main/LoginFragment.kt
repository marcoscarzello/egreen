package com.example.egreen_fragmentapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser








class LoginFragment : Fragment(R.layout.fragment_login) {

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

        val email  = view.findViewById<TextView>(R.id.mail)
        viewModel.currentuser.observe(this, Observer { u -> email.text = u?.email })
        //not registered yet? go to register activity!
        register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
    }
