package com.example.egreen_fragmentapplication.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import kotlinx.android.synthetic.main.fragment_plant_settings.*


class PlantSettingsFragment : Fragment(R.layout.fragment_plant_settings) {





    var plantType : TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val plantNameText  = view.findViewById<TextView>(R.id.plant_name)
        //val plantHeigthEditText  = view.findViewById<EditText>(R.id.Plant_height)
        val applyBtn  = view.findViewById<Button >(R.id.save_Button)
        //val plantTypeSpinner = view.findViewById<Spinner>(R.id.plant_Type)
        var plantImg = view.findViewById<ImageView>(R.id.plant_settings_image)
        var deletePlantBtn = view.findViewById<Button>(R.id.deletePlantButton)

        val plantTypeArray = resources.getStringArray(R.array.plant_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, plantTypeArray)

        (plantType?.editableText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)

        val autocompleteTV = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)

        /*
        deletePlantBtn.setOnClickListener {
            viewModel.deletePlant(viewModel.getSelectedPlantName())
            findNavController().navigate(R.id.action_plantSettingsFragment_to_gardenSettingsFragment)
        }

         */

        plantImg.setOnClickListener{
            viewModel.changeImgCalledFrom(2)    //qua dico che sto chiamando camera fragment da add plant
            findNavController().navigate(R.id.action_plantSettingsFragment_to_cameraFragment2)
        }
        viewModel.downPlantPic(requireContext(), plantImg)

        plantNameText.text = viewModel.getSelectedPlantName()
        plantNameText.setOnClickListener {
            Toast.makeText(
                this@PlantSettingsFragment.requireContext(),
                R.string.avviso_nome,
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.getSelectedPlantHeigth()
        viewModel.getSelectedPlantType()
        //viewModel.heigth.observe(this, Observer { h -> plantHeigthEditText.setText(h) })

        viewModel.plantType.observe(this, Observer { h ->  autocompleteTV.setText(h)})

        //plantHeigthEditText.setText(viewModel.getSelectedPlantHeigth())



        applyBtn.setOnClickListener{
            //viewModel.modifyPlant(plantHeigthEditText.text.toString())
            findNavController().navigate(R.id.action_plantSettingsFragment_to_plantFragment)
        }


        deletePlantBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this@PlantSettingsFragment.requireContext())
            // set message of alert dialog
            dialogBuilder.setMessage(R.string.plant_delete)
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener {
                        dialog, id ->


                    val ft = parentFragmentManager.beginTransaction()
                    // faccio cose per l'eliminazione della pianta

                    viewModel.deletePlant(viewModel.getSelectedPlantName())

                    Toast.makeText(
                        this@PlantSettingsFragment.requireContext(),
                        R.string.pianta_cancellata,
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(R.id.action_plantSettingsFragment_to_gardenSettingsFragment)

                })
                // negative button text and action
                .setNegativeButton(R.string.mistake, DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            var i = viewModel.getSelectedPlantName()
            alert.setTitle("Delete $i ?")
            // show alert dialog
            alert.show()

        }
    }
}