package com.example.egreen_fragmentapplication.ui.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.main_fragment.*
import androidx.lifecycle.Observer

import android.os.Handler
import android.widget.ImageView
import androidx.core.graphics.drawable.toIcon
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var cardArrayList:ArrayList<CardModel>
    private lateinit var adapter: CardAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    //private lateinit var viewModel: MainViewModel
    private lateinit var img1 : Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()
        viewModel.getPlants()
        viewModel.getPlantsUri()
        viewModel.getWtValues()
        viewModel.getHmValues()

        //Log.d("ListaUri", viewModel.plantListUri!!.value?.get(0)!!.toString())

        //var mutableRefDB = MutableLiveData<DatabaseReference?>()

        //viewModel.refDB.value?.child("plants")?.child("Pianta1")?.child("piantaimgUrl")
        //    ?.addValueEventListener(object: ValueEventListener{
        //        override fun onDataChange(snapshot: DataSnapshot) {
        //            val v = snapshot.getValue<String>()
        //            img1 = v!!.toUri()
        //            //Log.d("L'uri", img1.toString())
        //        }
        //        override fun onCancelled(error: DatabaseError) {}
        //    })

        //val img = mutableRefDB.value?.child("plants")?.child("Pianta1")?.child("piantaimgUrl").toString()

        //var humMap: MutableMap<String, String> = HashMap()
        //var watMap: MutableMap<String, String> = HashMap()

        val addPlant = view.findViewById<Button>(R.id.addPlant)

        addPlant.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_gardenSettingsFragment)        //ora va a garden settings per poter testare le cose
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

        val handler = Handler()

        cardArrayList = ArrayList()

        handler.postDelayed({ viewModel.plantList.observe(this, Observer { plantList ->
            if (plantList != null) {
                if (plantList.size < 1) {
                    Log.d("Oscar", "lista zero")
                }

                var i : Int = 0
                for (p: String in plantList) {
                        if (p != null) {

                            //viewModel.changeSelectedPlant(p)

                            cardArrayList.add(
                                CardModel(
                                    p,
                                    viewModel.plantListUri!!.value?.get(i)!!.toString(),//R.drawable.genoveffa,
                                    //viewModel.refDB.value?.child("plants")?.child(p)?.child("piantaimgUrl").toString().toUri(),
                                    viewModel.dataWtList!!.value?.get(i)!!.toString(),
                                    viewModel.dataHmList!!.value?.get(i)!!.toString()
                                )
                            )

                            //se non aspetto non va...
                            //handler.postDelayed({Log.d("Valore della pianta da frag",viewModel.wtlev.value.toString())}, 5)
                        }
                    i++
                    }
                cardArrayList.add(
                    CardModel(
                        "New Plant",
                        //R.drawable.genoveffa,
                        "test",//img1.toString(),//viewModel.refDB.value?.child("plants")?.child(p)?.child("piantaimgUrl").toString().toUri(),
                        "New Plant",
                        ""
                    )
                )

                //cardArrayList.add(
                //    CardModel(
                //        "New Plant",
                //        R.drawable.genoveffa.toString().toUri(),
                //        "Add Plant !",
                //        ""
                //    )
                //)

                adapter = CardAdapter(this.context, cardArrayList)
                viewPager.adapter = adapter
                viewPager.setPadding(100, 0, 100, 0)
                //Log.d("Pianta",viewPager.currentItem.toString())
                //viewModel.changeSelectedPlant("")
                }
            })
        }, 600)

        //cambio selectedPlant
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val nome = cardArrayList[position].plantName
                viewModel.changeSelectedPlant(nome)
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}