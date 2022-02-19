package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R



class GardenSettingsFragment : Fragment(R.layout.fragment_garden_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

        val plant0 = view.findViewById<Button>(R.id.plant0)
        val plant1 = view.findViewById<Button>(R.id.plant1)
        val plant2 = view.findViewById<Button>(R.id.plant2)
        val plant3 = view.findViewById<Button>(R.id.plant3)


        val plants= arrayOf(
            plant0,
            plant1,
            plant2,
            plant3

        )

        // se i button sono NEW PLANT, se cliccati mandano al frammento addPlant

        for (item: Button in plants){
            when (item.text){
                "new plant" ->
                item.setOnClickListener(){
                    findNavController().navigate(R.id.action_gardenSettingsFragment_to_addPlantFragment)
                }
            }

            //else -> mandano al PlantSettings della pianta cui si riferiscono
        }

}
}