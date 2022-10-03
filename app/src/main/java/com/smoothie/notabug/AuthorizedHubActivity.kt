package com.smoothie.notabug

import com.smoothie.notabug.authorized.CommunicationPagerFragment
import com.smoothie.notabug.authorized.ExplorePagerFragment
import com.smoothie.notabug.authorized.ProfileFragment

class AuthorizedHubActivity : HubActivity(R.menu.bottom_navigation_user, R.id.tab_explore) {
    override fun getFragment(itemId: Int): FadingFragment {
        return when(itemId) {
            R.id.tab_explore -> ExplorePagerFragment()
            R.id.tab_communication -> CommunicationPagerFragment()
            R.id.tab_profile -> ProfileFragment()
            else -> throw IllegalArgumentException("Unexpected item ID: $itemId")
        }
    }
}
