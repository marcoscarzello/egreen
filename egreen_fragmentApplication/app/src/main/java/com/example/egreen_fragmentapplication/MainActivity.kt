package com.example.egreen_fragmentapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.egreen_fragmentapplication.ui.main.MainFragment
import com.example.egreen_fragmentapplication.ui.main.MainViewModel
import com.example.egreen_fragmentapplication.ui.main.ProfileFragment
import com.example.egreen_fragmentapplication.ui.main.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    //prova notifica
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101


    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply{
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Pianta innaffiata!")
            .setContentText("La tua pianta è stata appena innaffiata dal sistema per carenza di umidità.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationID, builder.build())
        }
    }
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //notifica prova
        createNotificationChannel()
        val notificaBtn= findViewById<TextView>(R.id.notifica)
        notificaBtn.setOnClickListener{
            sendNotification()
        }
        //


        hideBottomBar(false)

        val vm: MainViewModel by viewModels()
        //prendo le info extra che ho messo nell'intent all'accesso nell'app e li assegno a variabili

        var userId: String? = ""
        var emailId: String? = ""

        val uid= findViewById<TextView>(R.id.userId)
        val eid= findViewById<TextView>(R.id.emailId)
        val logout = findViewById<TextView>(R.id.logoutBtn)

        //praticamente vedrò scritti mail con cui ho fatto l accesso e il corrispondente user ID
        //NOTA: li vedrò sempre perchè qui siamo nella main activity che è sempre attiva sotto ai vari fragment


        vm.currentuser.observe(this, Observer {user -> uid.text = user?.uid ; eid.text = user?.email})

        vm.initialize()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController


        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigationView1)




        nav.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.miHome -> navController.navigate(R.id.mainFragment)
                R.id.miProfile -> navController.navigate(R.id.profileFragment)
                R.id.miSettings -> navController.navigate(R.id.settingsFragment)
            }
            true
        }

    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
            commit()
        }

    fun hideBottomBar(isHidden: Boolean){
        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigationView1)
        nav.visibility = if (isHidden) View.GONE else View.VISIBLE
    }

}