package com.example.egreen_fragmentapplication.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AlertDialogLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.google.firebase.auth.FirebaseAuth

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val deleteAccount = view.findViewById<Button>(R.id.deleteAccountBtn)
        val logOut = view.findViewById<Button>(R.id.logoutBtn)
        val gardenSettings = view.findViewById<Button>(R.id.gardenSettingsBtn)
        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val email = view.findViewById<EditText>(R.id.accountEmail)
        val saveBtn = view.findViewById<Button>(R.id.saveAccountChanges)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)


        //MOSTRA ATTUALE MAIL
        email.setText(viewModel.getEmail())

        //MOSTRA ATTUALE USERNAME (EDITABLE)
        viewModel.username.observe(this, Observer { u -> etUsername.setText(u) })


        //click su password
        etPassword.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this@AccountSettingsFragment.requireContext())

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to change your password?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Yes, please", DialogInterface.OnClickListener {
                        dialog, id ->

                    //qua vorrò andare nel fragment di change password
                    findNavController().navigate(R.id.action_accountSettingsFragment_to_changePasswordFragment)

                })
                // negative button text and action
                .setNegativeButton("My mistake", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()

                    //qua bisognerebbe togliere il focus dal text password
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Your Password")
            // show alert dialog
            alert.show()
            //viewModel.deleteAccount()
        }

        saveBtn.setOnClickListener{
            viewModel.setUsername(etUsername.text.toString())
        }

        //LOGOUT
        logOut.setOnClickListener {
            //logout from app
            //FirebaseAuth.getInstance().signOut()

            viewModel.logOut()                                                                      //logOut funzione del MainViewModel
            findNavController().navigate(R.id.action_accountSettingsFragment_to_loginFragment)

            Toast.makeText(
                this@AccountSettingsFragment.requireContext(),
                "You're logged out successfully",
                Toast.LENGTH_SHORT
            ).show()
        }

        //TO GARDEN SETTINGS
        gardenSettings.setOnClickListener{
            findNavController().navigate(R.id.action_accountSettingsFragment_to_gardenSettingsFragment)
        }




        //DELETE ACCOUNT
        deleteAccount.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this@AccountSettingsFragment.requireContext())

            // set message of alert dialog
            dialogBuilder.setMessage("Your account will be completely deleted from the system")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("I'm sure", DialogInterface.OnClickListener {
                        dialog, id ->
                    viewModel.deleteAccount()
                    findNavController().navigate(R.id.action_accountSettingsFragment_to_registerFragment)
                    Toast.makeText(
                        this@AccountSettingsFragment.requireContext(),
                        "Account deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                })
                // negative button text and action
                .setNegativeButton("My mistake", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Are you sure?")
            // show alert dialog
            alert.show()
            //viewModel.deleteAccount()
        }
    }
}
