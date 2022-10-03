package com.smoothie.notabug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors

abstract class HubActivity(private val menuResource: Int, private val launchTabId: Int) : AppCompatActivity() {

    private lateinit var bottomNavigationBar : BottomNavigationView
    private lateinit var fragmentContainer : FragmentContainerView


    protected abstract fun getFragment(itemId: Int): FadingFragment

    private fun performTransaction(bottomBarItemId: Int) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container_view, getFragment(bottomBarItemId))
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)

        val surfaceColor = SurfaceColors.getColorForElevation(this, 8f)
        window.navigationBarColor = surfaceColor

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        fragmentContainer = findViewById(R.id.fragment_container_view)

        bottomNavigationBar.inflateMenu(menuResource)
        bottomNavigationBar.setOnItemSelectedListener { item ->
            performTransaction(item.itemId)
            true
        }

        performTransaction(launchTabId)
    }

}