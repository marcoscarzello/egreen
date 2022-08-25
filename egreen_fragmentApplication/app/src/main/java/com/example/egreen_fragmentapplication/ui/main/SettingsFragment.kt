package com.example.egreen_fragmentapplication.ui.main

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()
       //val accountBtn  = view.findViewById<Button >(R.id.plant0)
        val accountBtn = view.findViewById<ImageView>(R.id.plant00)
        val deviceBtn  = view.findViewById<Button >(R.id.plant1)
        //val networkBtn  = view.findViewById<Button >(R.id.plant2)
        val networkBtn  = view.findViewById<ImageView >(R.id.plant02)
        //val gardenBtn  = view.findViewById<Button >(R.id.plant3)
        val gardenBtn  = view.findViewById<ImageView >(R.id.plant03)
        val darkMode = view.findViewById<Switch>(R.id.darkMode)

        gardenBtn.setOnClickListener{
            findNavController().navigate(R.id.action_settingsFragment_to_gardenSettingsFragment)
        }

        accountBtn.setOnClickListener{
            findNavController().navigate(R.id.action_settingsFragment_to_accountSettingsFragment)
        }

        networkBtn.setOnClickListener{
            findNavController().navigate(R.id.action_settingsFragment_to_networkFragment)
        }

        /*
        viewModel.darkMode.observe(this, Observer { u ->
            //lo metto nella main activity

                if (u)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                darkMode.isChecked = u
            })
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {viewModel.setDarkMode(false)} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {viewModel.setDarkMode(true)} // Night mode is active, we're using dark theme
        }


         */
        viewModel.darkMode.observe(this, Observer { u ->
            darkMode.isChecked = u
        })

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {viewModel.setDarkMode(false)} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {viewModel.setDarkMode(true)} // Night mode is active, we're using dark theme
        }

        darkMode.setOnClickListener(View.OnClickListener {
                viewModel.setDarkMode(darkMode.isChecked)
        })

    }
}