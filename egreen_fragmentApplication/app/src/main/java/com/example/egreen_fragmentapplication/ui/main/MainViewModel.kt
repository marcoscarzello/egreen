package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

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

        plantData["plantName"] = plantName
        plantData["plantHeigth"] = plantHeight
        plantData["params"] = ""

        mutableRefDB.value?.child("plants")?.child(plantName)?.setValue(plantData)

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