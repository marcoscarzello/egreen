package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

data class User(var email : String, var plantName : String, var username : String){
}

class MainViewModel : ViewModel () {

    public var selectedPlant = "";

    private var mutableCurrentUser = MutableLiveData<FirebaseUser?>()
    val currentuser: LiveData<FirebaseUser?> get() = mutableCurrentUser

    private var mutableHeigth = MutableLiveData<String?>()
    val heigth: LiveData<String?> get() = mutableHeigth

    private var mutablePlantList = MutableLiveData<MutableList<String>>()
    val plantList: LiveData<MutableList<String>> get() = mutablePlantList

    //livedata per osservare umidità
    private var mutableHumidityMap = MutableLiveData<MutableMap<String, String>>()
    val humidityMap: LiveData<MutableMap<String, String>> get() = mutableHumidityMap

    private var mutableWaterMap = MutableLiveData<MutableMap<String, String>>()
    val waterMap: LiveData<MutableMap<String, String>> get() = mutableWaterMap

    open fun initialize(){
        mutablePlantList.value = mutableListOf()

    }

    private var mutableRefDB = MutableLiveData<DatabaseReference?>()
    val refDB: LiveData<DatabaseReference?> get() = mutableRefDB

     open fun updateCurrentUser(){
     //open fun getCurrentUser():MutableLiveData<FirebaseUser>{
        //if (FirebaseAuth.getInstance().currentUser != null )
            mutableCurrentUser.value =  FirebaseAuth.getInstance().currentUser


         mutableRefDB.value = Firebase.database.reference.child("users").child((currentuser.value?.uid.toString()))
    }



    open fun logOut(){
        FirebaseAuth.getInstance().signOut()
        updateCurrentUser()
        Log.d(TAG, "User logged out successfully.")
    }

    open fun addPlant(plantName: String, plantHeight: String){

        var plantData: MutableMap<String, String> = HashMap()
        var params: MutableMap<String, String> = HashMap()

        var provvisoria: MutableMap<String, String> = HashMap()     //da togliere quando si caricherà da arduino

        plantData["plantName"] = plantName
        plantData["plantHeigth"] = plantHeight

        params["last5humidity"] = ""
        params["last5waterlevel"] = ""


        provvisoria["a"] = "50"
        provvisoria["b"] = "45"
        provvisoria["c"] = "29"
        provvisoria["d"] = "26"
        provvisoria["e"] = "9"


        mutableRefDB.value?.child("plants")?.child(plantName)?.setValue(plantData)
        mutableRefDB.value?.child("plants")?.child(plantName)?.child("params")?.setValue(params)

        //da eliminare (simula ultimi dati caricati da arduino
        mutableRefDB.value?.child("plants")?.child(plantName)?.child("params")?.child("last5humidity")?.setValue(provvisoria)
        mutableRefDB.value?.child("plants")?.child(plantName)?.child("params")?.child("last5waterlevel")?.setValue(provvisoria)



    }

    open fun changeSelectedPlant(name: String) {

        selectedPlant = name

        mutableRefDB.value?.child("plants")?.child(selectedPlant)?.child("params")?.child("last5humidity")?.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                mutableHumidityMap.value = snapshot.getValue<MutableMap<String, String>>()
                Log.d("HUMIDITY MAP READ BY VM", humidityMap.value.toString())

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        mutableRefDB.value?.child("plants")?.child(selectedPlant)?.child("params")?.child("last5waterlevel")?.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                mutableWaterMap.value = snapshot.getValue<MutableMap<String, String>>()
                Log.d("Water MAP READ BY VM", waterMap.value.toString())

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    open fun getSelectedPlantHeigth() {
    //open fun getSelectedPlantHeigth() : String {
        var v: String = "default"
        mutableRefDB.value?.child("plants")?.child(selectedPlant)?.child("plantHeigth")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mutableHeigth.value = snapshot.getValue<String>().toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        //return heigth.value.toString()
    }

    open fun getSelectedPlantName() : String {
        return selectedPlant
    }

    open fun modifyPlant(plantHeight: String) {
        mutableRefDB.value?.child("plants")?.child(selectedPlant)?.child("plantHeigth")?.setValue(plantHeight)

    }

    open fun getPlants() {
    //open fun getPlants() : MutableList<String>?{
        //var plantList : MutableList<String> = mutableListOf()
        //mutablePlantList.value = mutableListOf()
        mutableRefDB.value?.child("plants")?.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val pianta = ds.child("plantName")?.getValue(String::class.java)
                    Log.d("DS", ds.toString())

                    Log.d("PIANTA", pianta.toString())

                    if (pianta != null && mutablePlantList.value?.contains(pianta) == false) mutablePlantList.value?.add(pianta)
                    Log.d("LISTA PAINTE", mutablePlantList.value.toString())

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        //return plantList
    }

    //DELETE ACCOUNT
    open fun deleteAccount(){
        val user = mutableCurrentUser!!.value
        val refDB = mutableRefDB.value
        logOut()
        Log.d( "User deleting account :" , user.toString())
        user?.delete()!!
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    refDB?.setValue(null)                       //cancella dal database ramo e sottorami relativi all'utente in questione
                }
            }

    }

    //TODO: update profile settings

    //TODO: get user data



/*
    private val userData: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUserData()
        }
    }

    fun getUserData(): LiveData<User> {
        return userData
    }

    private fun loadUserData() {
        var myDB = Firebase.database.reference
        val tmp =  FirebaseAuth.getInstance().currentUser?.uid.toString()
        //userData.email = myDB.child("users").child(tmp).child("email")
    }

 */
}