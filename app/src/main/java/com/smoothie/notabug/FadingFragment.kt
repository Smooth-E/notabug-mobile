package com.smoothie.notabug

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment

open class FadingFragment(layoutResourceId: Int) : Fragment(layoutResourceId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }
}