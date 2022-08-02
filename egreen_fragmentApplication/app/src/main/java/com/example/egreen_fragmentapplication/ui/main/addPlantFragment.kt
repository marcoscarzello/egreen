package com.example.egreen_fragmentapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import android.widget.ImageView
import androidx.core.net.toUri
import com.example.egreen_fragmentapplication.databinding.FragmentAddPlantBinding
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.Observer


/**
 * A simple [Fragment] subclass.
 * Use the [addPlantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//Alessandro
class addPlantFragment : Fragment(R.layout.fragment_add_plant) {

    val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentAddPlantBinding?= null
    private val binding get() = _binding!!

    private var mSpinner: Spinner? = null
    private var spinnerResult: String? = null
    private var mActivityCallback: ActivityInterface? = null
    var result: TextView? = null
    private var spinnerHeight: Spinner? = null
    var spinnerHeightResult:String? = null
    var plantName: String? = null
    var plantHeight: String? = null
    var plantType : TextView? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivityCallback = context as? ActivityInterface

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddPlantBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val plantTypeArray = resources.getStringArray(R.array.plant_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, plantTypeArray)

        (plantType?.editableText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)

        val autocompleteTV = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)


        val viewModel: MainViewModel by activityViewModels()
        result = view.findViewById(R.id.result)
        //spinnerHeight = view.findViewById(R.id.plant_height_spinner)
        //val heightEditText = view.findViewById(R.id.Plant_height) as EditText
        val nameEditText = view.findViewById(R.id.Plant_name) as EditText
        var photoImage = view.findViewById<ImageView>(R.id.plant_settings_image)

        viewModel.plantPicPath.observe(this, androidx.lifecycle.Observer { pp ->
            viewModel.downTakenPic(this@addPlantFragment.requireContext(), photoImage, pp)
        })

        photoImage.setOnClickListener {
            Log.d("Ho Cliccato ", "la foto")
            viewModel.changeImgCalledFrom(1)    //qua dico che sto chiamando camera fragment da add plant
            findNavController().navigate(R.id.action_addPlantFragment_to_cameraFragment2)
        }
        val saveButton = view.findViewById<Button>(R.id.save_Button)
        saveButton.text = "CREATE PLANT"
        saveButton.setOnClickListener {
            //apre la schermata del dettaglio pianta
            spinnerResult = mSpinner?.selectedItem as String?
            spinnerHeightResult = spinnerHeight?.selectedItem as String?
            plantName = nameEditText.text.toString()
            //plantHeight = heightEditText.text.toString()
            //plantType = spinnerResult


            // error for empty editText
            when {
                TextUtils.isEmpty(nameEditText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter plant name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
/*
                TextUtils.isEmpty(heightEditText.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter plant height.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

 */
                else -> {
                    //crea infine la pianta
                    viewModel.addPlant(
                        plantName.toString(),
                        plantHeight.toString(),
                        plantType.toString()
                    )
                    viewModel.changeSelectedPlant(plantName.toString())

                    findNavController().navigate(R.id.action_addPlantFragment_to_plantFragment)
                }
            }
            mActivityCallback?.onContinueButtonPressed()


        }

/*
        //IMMAGINE PIANTAAAA
        val openCamera = view.findViewById<Button>(R.id.photoAPI_Button)
        openCamera.text = "PHOTO RECOGNITION"
        openCamera.setOnClickListener {
        }


        setUpPlantSpinner()
        setUpHeightSpinner()
 */
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
