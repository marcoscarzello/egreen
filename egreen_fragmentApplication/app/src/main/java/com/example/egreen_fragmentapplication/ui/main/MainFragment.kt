package com.example.egreen_fragmentapplication.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


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
            /* QUUA PRATICAMENTE TOLGO IL VECCHIO E METTO IL NUOVO
                requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, addPlantFragment())
                //.addToBackStack("secondary")                         //fa sì che premendo il tasto back del dispositivo non si chiuda l'app ma si torni indietro
                .commitNow()

                    MOLTO PIU EASY - USO FINDNAVCONTROLLER.
             */

            findNavController().navigate(R.id.action_mainFragment_to_addPlantFragment)

        }


        //test FIREBASE
        val plantName = view.findViewById<TextView>(R.id.plantName)
        val db = Firebase.database.reference
        val ref = db.child("plantname")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val v = snapshot.getValue<String>()
                plantName.text = v
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}