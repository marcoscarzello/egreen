package com.example.egreen_fragmentapplication.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val deleteAccount = view.findViewById<TextView>(R.id.deleteAccountBtn)
        val logOut = view.findViewById<Button>(R.id.logoutBtn)
        //val gardenSettings = view.findViewById<Button>(R.id.gardenSettingsBtn)
        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val email = view.findViewById<EditText>(R.id.accountEmail)
        val saveBtn = view.findViewById<Button>(R.id.saveAccountChanges)
        val etPassword = view.findViewById<EditText>(R.id.current_password)
        val newpic = view.findViewById<Button>(R.id.newPic)
        val profilePic = view.findViewById<ImageView>(R.id.profile_pic_settings)

        val backButton = view.findViewById<ImageView>(R.id.backButton_accountSettings)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_accountSettingFragment_toSettingsFragment)
        }



        viewModel.downProfilePic(this@AccountSettingsFragment.requireContext(), profilePic )

        //MOSTRA ATTUALE MAIL
        email.setText(viewModel.getEmail())

        //MOSTRA ATTUALE USERNAME (EDITABLE)
        viewModel.username.observe(this, Observer { u -> etUsername.setText(u)
            if( u == null)
                etUsername.setText(viewModel.getEmail())
        })

        fun Context.hideKeyboard(view: View) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        //close keyboard on enter
        etUsername.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                context?.hideKeyboard(v)
            }
        }

        saveBtn.setOnClickListener{
            viewModel.setUsername(etUsername.text.toString())
            etUsername.clearFocus()
        }


        //LOGOUT
        logOut.setOnClickListener {
            //logout from app
            //FirebaseAuth.getInstance().signOut()

            viewModel.logOut(requireActivity())                                                                      //logOut funzione del MainViewModel
            findNavController().navigate(R.id.action_accountSettingsFragment_to_loginFragment)

            Toast.makeText(
                this@AccountSettingsFragment.requireContext(),
                "You're logged out successfully",
                Toast.LENGTH_SHORT
            ).show()

            //clear stack
            val intent = Intent(view.context, this@AccountSettingsFragment.requireActivity()::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
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
                    viewModel.deleteAccount(requireActivity())

                    findNavController().navigate(R.id.action_accountSettingsFragment_to_registerFragment)
                    Toast.makeText(
                        this@AccountSettingsFragment.requireContext(),
                        "Account deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    //clear stack
                    val intent = Intent(view.context, this@AccountSettingsFragment.requireActivity()::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

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

        newpic.setOnClickListener {
            viewModel.changeImgCalledFrom(0)    //qua dico che sto chiamando camera fragment da account settings
            findNavController().navigate(R.id.action_accountSettingsFragment_to_cameraFragment2)
        }



        //viewModel.downProfilePic(this@AccountSettingsFragment.requireContext(), profilePic)

    }


}
