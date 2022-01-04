package com.example.egreen_fragmentapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R


/**
 * A simple [Fragment] subclass.
 * Use the [addPlantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addPlantFragment : Fragment(R.layout.fragment_add_plant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<Button>(R.id.back)

        back.setOnClickListener{
            findNavController().navigate(R.id.action_addPlantFragment_to_mainFragment)
        }
    }


}