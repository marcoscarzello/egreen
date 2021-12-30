package com.example.egreen_fragmentapplication.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.egreen_fragmentapplication.R

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addPlant = view.findViewById<Button>(R.id.addPlant)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        addPlant.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, addPlantFragment())
                //.addToBackStack("secondary")                         //fa sì che premendo il tasto back del dispositivo non si chiuda l'app ma si torni indietro
                .commitNow()
        }
    }

}