package com.example.egreen_fragmentapplication.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainFragment : Fragment(R.layout.main_fragment) {



    companion object {
        fun newInstance() = MainFragment()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addPlant = view.findViewById<Button>(R.id.addPlant)
        val settings = view.findViewById<Button>(R.id.settingsBtn)
        val viewModel: MainViewModel by activityViewModels()


        addPlant.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_gardenSettingsFragment)        //ora va a garden settings per poter testare le cose
        }

        settings.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)              //anche questo per testare, poi non servirà
        }



        //test FIREBASE + autenticazione

/*
        val user =  FirebaseAuth.getInstance().currentUser?.uid.toString()      //recupero l'ID dello user corrente


        val db = Firebase.database.reference

        val ref = db.child("users").child((user)) //qui entro nel ramo degli utenti e poi in particolare dell'utente corrente

 */

        val plantName = view.findViewById<TextView>(R.id.plantName)

        viewModel.refDB.value?.child("plantName")?.addValueEventListener(object: ValueEventListener{    //refDB lo ricavo dal view model

        //ref.child("plantName").addValueEventListener(object: ValueEventListener{                              //qui accedo al valore di plantName e aggiorno un TextView con il valore presente nel database
            override fun onDataChange(snapshot: DataSnapshot) {
                val v = snapshot.getValue<String>()
                plantName.text = v
                                                                                                            //NOTA se ti sei appena registrato, nel db per ora non c'è il parametro plantName:
                                                                                                            // lo puoi aggiungere da firebase manualmente per verificare il funzionamento
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


/*
        val logout = view.findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener{
            //logout from app
            //FirebaseAuth.getInstance().signOut()

            viewModel.logOut()                                                                      //logOut funzione del MainViewModel
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)

            Toast.makeText(
                this@MainFragment.requireContext(),
                "You're logged out successfully",
                Toast.LENGTH_SHORT
            ).show()

        }

 */
    }
}