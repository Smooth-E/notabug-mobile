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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors

abstract class HubActivity(
    private val menuResource: Int,
    private val fragments: Array<Triple<Int, FadingFragment, String>>
    ) : LimitlessActivity() {



    companion object {
        private val KEY_SELECTED_TAB = "SelectedTabID"
    }

    private lateinit var bottomNavigationBar : BottomNavigationView
    private lateinit var fragmentContainer : FragmentContainerView

    private fun performTransaction(bottomBarItemId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        for (pair in fragments) {
            if (pair.first == bottomBarItemId) transaction.show(pair.second)
            else transaction.hide(pair.second)
        }
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        fragmentContainer = findViewById(R.id.fragment_container_view)

        fragmentContainer.doOnAttach {
            it.setPadding(
                it.paddingLeft,
                systemBarInsets.top + it.paddingTop,
                it.paddingRight,
                it.paddingBottom
            )
        }

        bottomNavigationBar.doOnAttach {
            it.setPadding(
                it.paddingLeft,
                it.paddingTop,
                it.paddingRight,
                systemBarInsets.bottom + it.paddingBottom
            )
        }
        bottomNavigationBar.inflateMenu(menuResource)
        bottomNavigationBar.setOnItemSelectedListener { item ->
            if (bottomNavigationBar.selectedItemId != item.itemId) performTransaction(item.itemId)
            true
        }

        Log.w("The amount of fragments", supportFragmentManager.fragments.size.toString())

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        for (triple in fragments) {
            val fragment = supportFragmentManager.findFragmentByTag(triple.third)
            if (fragment != null) transaction.remove(fragment)
            transaction.add(R.id.fragment_container_view, triple.second, triple.third)
            transaction.hide(triple.second)
        }
        transaction.commit()

        bottomNavigationBar.selectedItemId = fragments[0].first
        performTransaction(bottomNavigationBar.selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItemID = savedInstanceState.getInt(KEY_SELECTED_TAB)
        bottomNavigationBar.selectedItemId = selectedItemID
        performTransaction(selectedItemID)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_TAB, bottomNavigationBar.selectedItemId)
    }

}