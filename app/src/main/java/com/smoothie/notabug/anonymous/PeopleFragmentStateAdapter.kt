package com.smoothie.notabug.anonymous

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PeopleFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> UsersFragment()
            1 -> OrganizationsFragment()
            else -> throw IllegalAccessError("Unexpected position: $position")
        }
    }

}