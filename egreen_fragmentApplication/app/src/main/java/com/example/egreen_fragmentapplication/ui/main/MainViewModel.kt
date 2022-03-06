package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.BoolRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
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

    private var mutableType = MutableLiveData<String?>()
    val plantType: LiveData<String?> get() = mutableType

    private var mutablePlantList = MutableLiveData<MutableList<String>>()
    val plantList: LiveData<MutableList<String>> get() = mutablePlantList

    //livedata per osservare umidità
    private var mutableHumidityMap = MutableLiveData<MutableMap<String, String>>()
    val humidityMap: LiveData<MutableMap<String, String>> get() = mutableHumidityMap

    private var mutableWaterMap = MutableLiveData<MutableMap<String, String>>()
    val waterMap: LiveData<MutableMap<String, String>> get() = mutableWaterMap

    private var mutableUsername = MutableLiveData<String>()
    val username: LiveData<String> get() = mutableUsername

    private var mutabledataWtList = MutableLiveData<MutableList<String>>()
    val dataWtList: LiveData<MutableList<String>> get() = mutabledataWtList

    private var mutabledataHmList = MutableLiveData<MutableList<String>>()
    val dataHmList: LiveData<MutableList<String>> get() = mutabledataHmList

    open fun initialize(){
        mutablePlantList.value = mutableListOf()
        mutabledataWtList.value = mutableListOf()
        mutabledataHmList.value = mutableListOf()
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

    open fun addPlant(plantName: String, plantHeight: String, plantType : String){

        var plantData: MutableMap<String, String> = HashMap()
        var params: MutableMap<String, String> = HashMap()

        var provvisoria: MutableMap<String, String> = HashMap()     //da togliere quando si caricherà da arduino

        plantData["plantName"] = plantName
        plantData["plantHeigth"] = plantHeight
        plantData["plantType"] = plantType

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
    open fun getSelectedPlantType() {

        mutableRefDB.value?.child("plants")?.child(selectedPlant)?.child("plantType")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mutableType.value = snapshot.getValue<String>().toString()
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

    //private var mutableWtlev = MutableLiveData<String>()
    //val wtlev: LiveData<String> get() = mutableWtlev

    open fun getWtValues(){

        mutableRefDB.value?.child("plants")?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val pianta = ds.child("plantName")?.getValue(String::class.java)

                    if (pianta != null) {
                        mutableRefDB.value?.child("plants")?.child(pianta)?.child("params")?.child("last5waterlevel")?.child("a")?.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val wt = snapshot.getValue<String>().toString()
                                Log.d("Il wt lev", wt)
                                mutabledataWtList.value?.add(snapshot.getValue<String>().toString())
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {} })
    }

    open fun getHmValues(){

        mutableRefDB.value?.child("plants")?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val plant = ds.child("plantName")?.getValue(String::class.java)

                    if (plant != null) {
                        mutableRefDB.value?.child("plants")?.child(plant)?.child("params")?.child("last5humidity")?.child("a")?.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val hm = snapshot.getValue<String>().toString()
                                Log.d("Il hm lev", hm)
                                mutabledataHmList.value?.add(snapshot.getValue<String>().toString())
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {} })
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
    open fun getEmail(): String {
        return mutableCurrentUser.value?.email.toString()
    }

    open fun getUsername() {

        mutableRefDB.value?.child("username")?.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    mutableUsername.value = snapshot.getValue<String>().toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    open fun setUsername(username: String){
        mutableRefDB.value?.child("username")?.setValue(username)

    }


    //CHANGE PSW
     open fun changePsw(newPsw: String){

        mutableCurrentUser.value!!.updatePassword(newPsw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    open fun reAuthUser(current_password: String): Boolean{
        Log.d("currentpsw in VM: ", current_password)
        var success = false
        if(!current_password.isEmpty() && current_password != null) {
            Log.d("entrato nell if VM", "yes")
            Log.d("EMAIL", mutableCurrentUser.value?.email.toString())
            val credential: AuthCredential = EmailAuthProvider.getCredential(mutableCurrentUser.value?.email.toString(), current_password)
            Log.d("CREDENTIAL", credential.toString())


            mutableCurrentUser.value!!.reauthenticate(credential).addOnSuccessListener {
                        success = true
                        Log.d(TAG, "User re-authenticated.")}.addOnFailureListener{

                        success = false
                        Log.d(TAG, "not  authhenticated CAZZO")
                    }


        }
        Log.d("SUCCESSSSSS: ", success.toString())
        return success
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