package com.example.egreen_fragmentapplication.ui.main

import android.app.Activity.RESULT_OK
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.media.MediaBrowserServiceCompat.RESULT_OK
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.MainActivity
import com.example.egreen_fragmentapplication.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }}

       /*

        TENTATIVO FALLITO

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
