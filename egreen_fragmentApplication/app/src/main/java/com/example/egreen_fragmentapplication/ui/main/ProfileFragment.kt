package com.example.egreen_fragmentapplication.ui.main

import android.app.Activity.RESULT_OK
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.media.MediaBrowserServiceCompat.RESULT_OK
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        val nametextview = view.findViewById<TextView>(R.id.username_profile)
        val noplants = view.findViewById<TextView>(R.id.no_plants)
        val oxigen = view.findViewById<TextView>(R.id.oxigen)
        val greenscore = view.findViewById<TextView>(R.id.greenScore)
        val profilePic = view.findViewById<ImageView>(R.id.profile_pic)
        val editProfile = view.findViewById<ImageView>(R.id.edit_profile)


        editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_accountSettingsFragment)
        }

        viewModel.username.observe(this, Observer { u -> nametextview.text = u })

        viewModel.plantList.observe(this, Observer {plantList ->
            noplants.text = plantList.count().toString()


            oxigen.text = (plantList.count() * 2.4).toString()
            //greenscore?
            var tmp = plantList.count()*100*2.4*0.7
            greenscore.text = tmp.toInt().toString()


        })

        //oxigen come si misura?


        viewModel.downProfilePic(this@ProfileFragment.requireContext(), profilePic )        //aggiorna realtime l'immagine profilo in base a quella presente su firestore

        }}

       /*

        TENTATIVO FALLITO DI CAMBIAMENTO IMMAGINE CHE TANTO COMUNQUE È DA FARE IN ACCOUNT SETTINGS SOLO

        val PICK_IMAGE_REQUEST = 1

        val changePicBtn = view.findViewById<Button>(R.id.change_pic_btn)
        val uploadPicBtn = view.findViewById<Button>(R.id.upload_pic_btn)
        val profilePic = view.findViewById<ImageView>(R.id.profile_pic)
        var imageUri : String = ""

        changePicBtn.setOnClickListener{
            openFileChooser()
        }

    }

    fun openFileChooser(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getResult.launch(intent)

    }
    val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == MainActivity.RESULT_OK){
                val value = it.data?.getStringExtra("input")
            }
        }

        */*/
