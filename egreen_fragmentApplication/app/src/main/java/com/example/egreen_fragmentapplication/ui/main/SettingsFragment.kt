package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Switch
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

        val accountBtn  = view.findViewById<Button >(R.id.plant0)
        val deviceBtn  = view.findViewById<Button >(R.id.plant1)
        val networkBtn  = view.findViewById<Button >(R.id.plant2)
        val gardenBtn  = view.findViewById<Button >(R.id.plant3)
        val darkMode = view.findViewById<Switch>(R.id.darkMode)

        accountBtn.setOnClickListener{
            findNavController().navigate(R.id.action_addPlantFragment_to_mainFragment)
        }
        }
}