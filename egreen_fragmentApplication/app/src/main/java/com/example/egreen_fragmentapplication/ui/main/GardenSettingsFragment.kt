package com.example.egreen_fragmentapplication.ui.main

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R



class GardenSettingsFragment : Fragment(R.layout.fragment_garden_settings) {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

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




        viewModel.plantList.observe(this, Observer {plantList ->


            if (plantList != null) {
                var i : Int = 0
                for (p: String in plantList)
                {
                    var j : Int = 0
                    for (b : Button in plants){

                        if (i == j) {
                            if (p != null) {
                                b.text = p
                                Log.d("Garden", "aassegndo")
                            } else
                                b.text = "new plant"
                        }

                        j++
                    }
                    i++
                }
            }
            Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", plantList.toString())


            for (item: Button in plants){
                when (item.text){
                    "new plant" ->
                        item.setOnClickListener(){
                            viewModel.resetTmpPlantPath()
                            findNavController().navigate(R.id.action_gardenSettingsFragment_to_addPlantFragment)
                        }

                    else -> {
                        item.setOnClickListener() {

                            viewModel.changeSelectedPlant(item.text.toString())
                            findNavController().navigate(R.id.action_gardenSettingsFragment_to_plantSettingsFragment)
                        }
                        item.setOnLongClickListener {

                            val dialogBuilder = AlertDialog.Builder(this@GardenSettingsFragment.requireContext())
                            // set message of alert dialog
                            dialogBuilder.setMessage("This plant will be deleted from the system")
                                // if the dialog is cancelable
                                .setCancelable(false)
                                // positive button text and action
                                .setPositiveButton("Yes", DialogInterface.OnClickListener {
                                        dialog, id ->

                                    // faccio cose per l'eliminazione della pianta

                                    viewModel.deletePlant(item.text.toString())
                                    //qua va reloaddato il tutto se no vedo ancora la pianta anche se non esiste piuuuu

                                    Toast.makeText(
                                        this@GardenSettingsFragment.requireContext(),
                                        "Plant deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //qua va reloaddato il fragment di garden settings (e non solo)

                                })
                                // negative button text and action
                                .setNegativeButton("My mistake", DialogInterface.OnClickListener {
                                        dialog, id -> dialog.cancel()
                                })

                            // create dialog box
                            val alert = dialogBuilder.create()
                            // set title for alert dialog box
                            var i = item.text
                            alert.setTitle("Delete $i ?")
                            // show alert dialog
                            alert.show()
                            //viewModel.deleteAccount()

                            true
                        }
                    }
                }

                //else -> mandano al PlantSettings della pianta cui si riferiscono
            }

            /*

            if (plantList.get(0) != null) plant0.text = plantList.get(0)
            else plant0.text = "new plant"
            if (plantList.get(1) != "") plant1.text = plantList.get(1)
            else plant1.text = "new plant"
            if (plantList.get(2) != "") plant2.text = plantList.get(2)
            else plant2.text = "new plant"
            if (plantList.get(3) != "") plant3.text = plantList.get(3)
            else plant3.text = "new plant"
               */

        })





    }

}