package com.smoothie.notabug

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CodeFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2;

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RepositoriesFragment()
            1 -> MirrorsFragment()
            else -> throw IllegalAccessError("Unexpected position: $position")
        }
    }
}
