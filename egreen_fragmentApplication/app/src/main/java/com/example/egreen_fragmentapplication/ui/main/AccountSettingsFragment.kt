package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R

class AccountSettingsFragment : Fragment(R.layout.fragment_account_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val deleteAccount = view.findViewById<Button>(R.id.deleteAccountBtn)
        val logOut = view.findViewById<Button>(R.id.logoutBtn)

        logOut.setOnClickListener {
            //logout from app
            //FirebaseAuth.getInstance().signOut()

            viewModel.logOut()                                                                      //logOut funzione del MainViewModel
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)

            Toast.makeText(
                this@AccountSettingsFragment.requireContext(),
                "You're logged out successfully",
                Toast.LENGTH_SHORT
            ).show()


        }
    }
}
