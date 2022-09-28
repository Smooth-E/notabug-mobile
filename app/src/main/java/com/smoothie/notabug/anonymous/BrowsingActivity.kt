package com.smoothie.notabug.anonymous

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smoothie.notabug.R

class BrowsingActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_main)

        bottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_code -> { true }
                R.id.tab_people -> { true }
                R.id.tab_settings -> { true }
                else -> false
            }
        }
    }

}