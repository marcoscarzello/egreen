package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R


class PlantFragment : Fragment(R.layout.fragment_plant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()


        var humMap: MutableMap<String, String> = HashMap()
        var watMap: MutableMap<String, String> = HashMap()


        val plantNameText  = view.findViewById<TextView>(R.id.plant_title)
        val humidity  = view.findViewById<TextView>(R.id.humidity)
        val waterlevel  = view.findViewById<TextView>(R.id.water_level)


        plantNameText.text = viewModel.getSelectedPlantName()

        viewModel.humidityMap.observe(this, Observer { hm ->
            humMap = hm
            humidity.text = humMap["e"]
            Log.d("HUMIDITY MAP READ BY FRAGMENT PLANT", humMap.toString())

        })

        viewModel.waterMap.observe(this, Observer { wm ->
            watMap = wm
            waterlevel.text = watMap["e"]
            Log.d("Water MAP READ BY FRAGMENT PLANT", watMap.toString())

        })
    }
}