package com.example.egreen_fragmentapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.egreen_fragmentapplication.ui.main.MainFragment

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
    }
}