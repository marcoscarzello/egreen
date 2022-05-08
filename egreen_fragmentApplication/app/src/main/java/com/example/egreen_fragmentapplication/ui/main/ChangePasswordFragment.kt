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
import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels


class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val currentPsw = view.findViewById<EditText>(R.id.current_password)
        val newPsw = view.findViewById<EditText>(R.id.new_password)
        val newPswAgain = view.findViewById<EditText>(R.id.new_psw_again)


        currentPsw.doAfterTextChanged {
            psw ->
            Log.d("currentpsw in change password fragment: ", psw.toString())
            newPsw.isFocusable = viewModel.reAuthUser(psw.toString())  //negative feedback - password errata
            Log.d("is focusable : ", viewModel.reAuthUser(psw.toString()).toString())


        }




    }
}