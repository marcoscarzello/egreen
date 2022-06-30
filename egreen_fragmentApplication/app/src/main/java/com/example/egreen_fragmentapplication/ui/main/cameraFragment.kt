package com.example.egreen_fragmentapplication.ui.main

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import java.io.File


class CameraFragment : Fragment(R.layout.fragment_camera) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        var imag = view.findViewById<ImageView>(R.id.capturedImage)

        viewModel.plantPicPath.observe(this, Observer { pp->  viewModel.downTakenPic(this@CameraFragment.requireContext(), imag, pp)  })
        viewModel.downTakenPic(this@CameraFragment.requireContext(), imag, viewModel.plantPicPath.value)

    //TEST IMMAGINI

        //val gallery = view.findViewById<Button>(R.id.galleria)
        //val camera = view.findViewById<Button>(R.id.btnTakePicture)
        val gallery = view.findViewById<ImageView>(R.id.gallery)
        val camera = view.findViewById<ImageView>(R.id.cam)

        val closeBtn = view.findViewById<ImageButton>(R.id.closeCamera)


        gallery.setOnClickListener {
            pickImageGallery()
        }

        camera.setOnClickListener {
            openCamera()
        }

        closeBtn.setOnClickListener{
            //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            //this.fragmentManager?.beginTransaction()?.remove(this)?.commit()
            findNavController().popBackStack()
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
            Log.e("DATA", data?.data.toString())


            viewModel.uploadPic(this@CameraFragment.requireContext(), data?.data!!)
            //viewModel.tmpImgUrl = data?.data!!


        }
    }

    private val FILE_NAME = "photo.jpg"
    private lateinit var photoFile: File

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val viewModel: MainViewModel by activityViewModels()

        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

        val file = File(photoFile.absolutePath)
        val uri = Uri.fromFile(file)

        //val rotatedBitmap = rotateBitmap(bitmap, 90f)
        //val uri = photoFile.absolutePath
        //val uri = it?.data?.data
        //Log.d("L'uri", uri.toString())
        //val uri = it?.data?.data

        viewModel.uploadPic(this@CameraFragment.requireContext(), uri)
    }

    private fun openCamera() {
        Log.d("Questa", "è la modifica")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(requireContext(), "com.example.egreen_fragmentapplication.ui.main.fileprovider", photoFile )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        getAction.launch(intent)
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

}



