package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R


class PlantSettingsFragment : Fragment(R.layout.fragment_plant_settings) {





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val plantNameText  = view.findViewById<TextView>(R.id.plant_name)
        val plantHeigthEditText  = view.findViewById<EditText>(R.id.Plant_height)
        val applyBtn  = view.findViewById<Button >(R.id.save_Button)
        val plantTypeSpinner = view.findViewById<Spinner>(R.id.plant_Type)
        var plantImg = view.findViewById<ImageView>(R.id.plant_settings_image)

        viewModel.downPlantPic(requireContext(), plantImg)

        plantNameText.text = viewModel.getSelectedPlantName()
        viewModel.getSelectedPlantHeigth()
        viewModel.getSelectedPlantType()
        viewModel.heigth.observe(this, Observer { h -> plantHeigthEditText.setText(h) })

        //viewModel.plantType.observe(this, Observer { h -> plantTypeSpinner.setText(h) })

        //plantHeigthEditText.setText(viewModel.getSelectedPlantHeigth())



        applyBtn.setOnClickListener{
            viewModel.modifyPlant(plantHeigthEditText.text.toString())
            findNavController().navigate(R.id.action_plantSettingsFragment_to_plantFragment)
        }

    }
}