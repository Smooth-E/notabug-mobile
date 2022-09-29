package com.smoothie.notabug.anonymous

import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R

class BrowsingActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationView
    private lateinit var fragmentContainer : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_main)

        val surfaceColor = SurfaceColors.getColorForElevation(this, 8f)
        window.navigationBarColor = surfaceColor

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        fragmentContainer = findViewById(R.id.fragment_container_view)

        bottomNavigationBar.setOnItemSelectedListener { item ->
            supportFragmentManager.commit {
                setCustomAnimations(R.anim.alpha_fade_in, R.anim.alpha_fade_out)
                val viewID = R.id.fragment_container_view
                when (item.itemId) {
                    R.id.tab_code -> replace<CodeFragment>(viewID)
                    R.id.tab_people -> replace<PeopleFragment>(viewID)
                    R.id.tab_settings -> replace<SettingsFragment>(viewID)
                    else -> throw IllegalAccessError("Unexpected id: ${item.itemId}")
                }
                setReorderingAllowed(true)
            }
            true
        }
    }
}