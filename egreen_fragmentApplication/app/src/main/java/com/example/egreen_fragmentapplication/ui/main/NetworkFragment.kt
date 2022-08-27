package com.example.egreen_fragmentapplication.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback

import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class NetworkFragment : Fragment(R.layout.fragment_network) {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //se faccio indietro da network fragment andro sempre in settings fragmnet
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Log.d(TAG, "Fragment back pressed invoked")
                        // Do custom work here
                        findNavController().navigate(R.id.action_networkFragment_to_settingsFragment)

                    }
                }
                )


        val connectBtn = view.findViewById<Button>(R.id.connect_button)
        val userCode = view.findViewById<TextView>(R.id.user_code)

        val viewModel: MainViewModel by activityViewModels()


        viewModel.currentuser.observe(this, Observer {user ->
            userCode.text = user?.uid})

        connectBtn.setOnClickListener{
            val url = "http://192.168.4.1"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        userCode.setOnClickListener {
            copyText(userCode)
                    }


    }
    fun copyText(userCode: TextView) {
        val text = userCode.text.toString()
        if (text.isNotEmpty()) {
            val clipboardManager =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("key", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this@NetworkFragment.context, "Copied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@NetworkFragment.context, "No text to be copied", Toast.LENGTH_SHORT).show()
        }
    }
}