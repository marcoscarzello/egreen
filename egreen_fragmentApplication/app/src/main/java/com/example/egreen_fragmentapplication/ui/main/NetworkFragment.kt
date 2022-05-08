package com.example.egreen_fragmentapplication.ui.main

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
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class NetworkFragment : Fragment(R.layout.fragment_network) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectBtn = view.findViewById<Button>(R.id.connect_button)
        val userCode = view.findViewById<TextView>(R.id.user_code)

        val viewModel: MainViewModel by activityViewModels()


        viewModel.currentuser.observe(this, Observer {user ->
            userCode.text = user?.uid})

        connectBtn.setOnClickListener{
            val url = "http://www.polito.it"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    }
}