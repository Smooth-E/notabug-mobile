package com.smoothie.notabug

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnAttach
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors

abstract class HubActivity(private val menuResource: Int, private val launchTabId: Int) : LimitlessActivity() {

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

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        fragmentContainer = findViewById(R.id.fragment_container_view)

        fragmentContainer.doOnAttach { it.setPadding(it.paddingLeft, systemBarInsets.top + it.paddingTop, it.paddingRight, it.paddingBottom) }

        bottomNavigationBar.doOnAttach { it.setPadding(it.paddingLeft, it.paddingTop, it.paddingRight, systemBarInsets.bottom + it.paddingBottom) }
        bottomNavigationBar.inflateMenu(menuResource)
        bottomNavigationBar.setOnItemSelectedListener { item ->
            performTransaction(item.itemId)
            true
        }

        performTransaction(launchTabId)
    }

}