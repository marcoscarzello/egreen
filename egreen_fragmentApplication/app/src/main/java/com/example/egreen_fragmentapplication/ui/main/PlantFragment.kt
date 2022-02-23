package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R


class PlantFragment : Fragment(R.layout.fragment_plant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        j
        val plantNameText  = view.findViewById<TextView>(R.id.plant_title)
        val humidity  = view.findViewById<TextView>(R.id.humidity)
        val waterlevel  = view.findViewById<TextView>(R.id.water_level)



    }
}