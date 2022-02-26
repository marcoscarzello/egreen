package com.example.egreen_fragmentapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


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

        val homeFr = MainFragment()
        val profileFr = ProfileFragment()
        val settingsFr = SettingsFragment()



        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigationView1)

        nav.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.miHome -> navController.navigate(R.id.mainFragment)
                R.id.miProfile -> navController.navigate(R.id.accountSettingsFragment)
                R.id.miSettings -> navController.navigate(R.id.settingsFragment)
            }
            true
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.mainFragment){
                if(FirebaseAuth.getInstance().currentUser == null){
                    //start LoginFragment
                    //hide bottom navigation
                    nav.visibility = View.GONE
                    Log.d("current user NULL:", FirebaseAuth.getInstance().currentUser.toString())
                }else{
                    //show bottom navigation
                    nav.visibility = View.VISIBLE
                    Log.d("current user NOT NULL:", FirebaseAuth.getInstance().currentUser.toString())

                }
            }
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