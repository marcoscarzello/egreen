package com.example.egreen_fragmentapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = Firebase.database.reference
        val ref = db.child("users") // primo ramo del database sotto cui ci saranno tutti i vari utenti (ciascuno con userID unico)


        var r_username = findViewById<EditText>(R.id.username)
        var r_email = findViewById<EditText>(R.id.email)
        var r_password = findViewById<EditText>(R.id.password)
        var registerButton = findViewById<Button>(R.id.registerButton)


        val login = findViewById<TextView>(R.id.login)

        //registered already? go to login activity!
        login.setOnClickListener {
          startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }


       registerButton.setOnClickListener {
           when {
               TextUtils.isEmpty(r_email.text.toString().trim { it <= ' ' }) -> {
                   Toast.makeText(
                       this@RegisterActivity,
                       "Please enter email.",
                       Toast.LENGTH_SHORT
                   ).show()
               }

               TextUtils.isEmpty(r_username.toString().trim { it <= ' ' }) -> {
                   Toast.makeText(
                       this@RegisterActivity,
                       "Please enter username.",
                       Toast.LENGTH_SHORT
                   ).show()
               }

               TextUtils.isEmpty(r_password.toString().trim { it <= ' ' }) -> {
                   Toast.makeText(
                       this@RegisterActivity,
                       "Please enter password.",
                       Toast.LENGTH_SHORT
                   ).show()
               }

               else -> {

                   val email: String = r_email.text.toString().trim { it <= ' ' }
                   val password: String = r_password.text.toString().trim { it <= ' ' }

                   FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(
                           OnCompleteListener<AuthResult> { task ->


                               //if registration succesfully done
                               if (task.isSuccessful) {

                                   //Firebase registered user
                                   val firebaseUser: FirebaseUser = task.result!!.user!!

                                   val currentUserDb = ref.child((firebaseUser.uid))    //sottoramo di users che ha come chiave l'userID assegnato al nuovo utente
                                   currentUserDb.child("username")?.setValue(r_username.text.toString())        //metto nel ramo dell'utente creato lo username
                                   currentUserDb.child("email")?.setValue(r_email.text.toString())              //metto nel ramo dell'utente creato la mail

                                   Toast.makeText(
                                       this@RegisterActivity,
                                       "You are registered succesfully",
                                       Toast.LENGTH_SHORT
                                   ).show()


                                   val intent =
                                       Intent(this@RegisterActivity, MainActivity::class.java)
                                   intent.flags =
                                       Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                   intent.putExtra("user_id", firebaseUser.uid)
                                   intent.putExtra("email_id", email)
                                   startActivity(intent)
                                   finish()
                               } else {
                                   //if registering is not successful then show error message
                                   Toast.makeText(
                                       this@RegisterActivity,
                                       task.exception!!.message.toString(),
                                       Toast.LENGTH_SHORT
                                   ).show()
                               }
                           })
               }
           }
       }
    }
}
