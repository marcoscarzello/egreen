package com.example.egreen_fragmentapplication.ui.main

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.egreen_fragmentapplication.R


class CameraFragment : Fragment(R.layout.fragment_camera) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        var imag = view.findViewById<ImageView>(R.id.capturedImage)

        viewModel.downProfilePic(this@CameraFragment.requireContext(), imag)

    //TEST IMMAGINI

    val gallery = view.findViewById<Button>(R.id.galleria)
    gallery.setOnClickListener {

        pickImageGallery()
    }
}


    companion object{
        val IMAGE_REQUEST_CODE = 100
    }
    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val viewModel: MainViewModel by activityViewModels()
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            Log.d("DATA", data?.data.toString())


            viewModel.uploadPic(this@CameraFragment.requireContext(), data?.data!!)



        }
    }}



