package com.smoothie.notabug

import android.graphics.Insets
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.RecyclerView

open class LimitlessActivity : AppCompatActivity() {

    protected lateinit var insets: WindowInsets
    protected lateinit var systemBarInsets : Insets

    private fun manageLimits() {
        window.decorView.doOnAttach {
            insets = it.rootWindowInsets
            systemBarInsets = insets.getInsets(WindowInsets.Type.systemBars())
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        manageLimits()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manageLimits()
    }

    fun systemBarInsets(): Insets = systemBarInsets

}