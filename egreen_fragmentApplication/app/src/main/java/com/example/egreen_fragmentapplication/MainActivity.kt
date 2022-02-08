package com.example.egreen_fragmentapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.egreen_fragmentapplication.ui.main.MainFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MainFragment.newInstance())
                .commitNow()
        }

         */

        //prendo le info extra che ho messo nell'intent all'accesso nell'app e li assegno a variabili

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        val uid= findViewById<TextView>(R.id.userId)
        val eid= findViewById<TextView>(R.id.emailId)
        val logout = findViewById<TextView>(R.id.logoutBtn)

        //praticamente vedrò scritti mail con cui ho fatto l accesso e il corrispondente user ID
        uid.text = "User ID :: $userId"
        eid.text = "Email ID :: $emailId"

        logout.setOnClickListener{
            //logout from app
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }


    }
}