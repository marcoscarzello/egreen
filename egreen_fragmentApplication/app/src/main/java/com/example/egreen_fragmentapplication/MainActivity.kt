package com.example.egreen_fragmentapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.egreen_fragmentapplication.ui.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



        val vm: MainViewModel by viewModels()
        //prendo le info extra che ho messo nell'intent all'accesso nell'app e li assegno a variabili

        var userId: String? = ""
        var emailId: String? = ""

        val uid= findViewById<TextView>(R.id.userId)
        val eid= findViewById<TextView>(R.id.emailId)
        val logout = findViewById<TextView>(R.id.logoutBtn)

        //praticamente vedrò scritti mail con cui ho fatto l accesso e il corrispondente user ID
        //NOTA: li vedrò sempre perchè qui siamo nella main activity che è sempre attiva sotto ai vari fragment


        vm.user.observe(this, Observer {user -> uid.text = user.uid ; eid.text = user.email})


    }
}