package com.example.egreen_fragmentapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.example.egreen_fragmentapplication.ui.main.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //NOTIFICA NON LA USIAMOOOOO
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply{
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun sendNotification(plant: String){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Low water in $plant!")
            .setContentText("The water level of your tank is below 25%! Fill it up quickly!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationID, builder.build())
        }
    }
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val vm: MainViewModel by viewModels()

        supportActionBar?.hide()

        //notifica prova
        createNotificationChannel()


        //provo darkmode
        //vm.setDarkMode(false)
        //vm.getDarkMode()


        hideBottomBar(false)



        vm.darkMode.observe(this, Observer { u ->
            //lo metto nella main activity
            Log.e("darkmode", u.toString())
            if(u !== null) {
                if (u)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {vm.setDarkMode(false)} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {vm.setDarkMode(true)} // Night mode is active, we're using dark theme
        }

/*
        //notifica

        vm.water.observe(this, Observer{ w->
            if(w.toInt() < 25){
                sendNotification(vm.getSelectedPlantName())
            }
        })

 */

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