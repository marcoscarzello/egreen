package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import com.example.egreen_fragmentapplication.R


/**
 * A simple [Fragment] subclass.
 * Use the [addPlantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//Alessandro
class addPlantFragment : Fragment(R.layout.fragment_add_plant) {

    private var mSpinner: Spinner? = null
    private var spinnerResult: String? = null
    private var mActivityCallback: ActivityInterface? = null
    var result: TextView? = null
    private var spinnerHeight: Spinner? = null
    var spinnerHeightResult:String? = null
    var plantName: String? = null
    var plantHeight: String? = null

    //Alessandro
    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_plant, container, false)
    }

     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result = view.findViewById(R.id.result)
        mSpinner = view.findViewById(R.id.plant_Type)
        spinnerHeight = view.findViewById(R.id.plant_height_spinner)
        val heightEditText = view.findViewById(R.id.Plant_height) as EditText
        val nameEditText = view.findViewById(R.id.Plant_name) as EditText
        val photoImage = view.findViewById<ImageView>(R.id.plant_image)
        photoImage.setOnClickListener {
            mActivityCallback?.onOpenCameraPressed()
        }
        val saveButton = view.findViewById<Button>(R.id.save_Button)
        saveButton.text = "CREATE PLANT"
        saveButton.setOnClickListener{
            //apre la schermata del dettaglio pianta
            spinnerResult = mSpinner?.selectedItem as String?
            spinnerHeightResult = spinnerHeight?.selectedItem as String?
            plantName = nameEditText.text.toString()
            plantHeight = heightEditText.text.toString()

            // error for empty editText
            when {
                TextUtils.isEmpty(nameEditText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter plant name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(heightEditText.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter plant height.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            mActivityCallback?.onContinueButtonPressed()
        }

        val openCamera = view.findViewById<Button>(R.id.photo_Button)
        openCamera.text = "PHOTO RECOGNITION"
        openCamera.setOnClickListener {
            mActivityCallback?.onOpenCameraPressed()
        }
        setUpPlantSpinner()
        setUpHeightSpinner()
    }

    private fun setUpPlantSpinner() {


        val arrayList = arrayOf("Pianta Grassa", "Pianta Tropicale", "Pianta Carnivora", "Ent", "Altro tipo di pianta")
        mSpinner?.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, arrayList)
        mSpinner?.prompt = "Tipo di pianta"
        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //salva il risultato dello spinner
                spinnerResult = mSpinner?.selectedItem as String?
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //non serve che succeda nulla
                TODO()
            }
        }

    }

    private fun setUpHeightSpinner() {
        val arrayList = arrayOf("cm", "m", "mm")
        spinnerHeight?.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, arrayList)
        spinnerHeight?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerHeightResult = mSpinner?.selectedItem as String?
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //non serve che succeda nulla
                TODO()
            }
        }

    }

    interface ActivityInterface {
        fun onContinueButtonPressed()
        fun onOpenCameraPressed()
    }





}