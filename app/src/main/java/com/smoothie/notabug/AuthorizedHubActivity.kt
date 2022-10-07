package com.smoothie.notabug

import com.smoothie.notabug.authorized.CommunicationPagerFragment
import com.smoothie.notabug.authorized.ExplorePagerFragment
import com.smoothie.notabug.authorized.ProfileFragment

class AuthorizedHubActivity : HubActivity(
    R.menu.bottom_navigation_user,
    arrayOf(
        Triple(R.id.tab_explore, ExplorePagerFragment(), "Explore Fragment"),
        Triple(R.id.tab_communication, CommunicationPagerFragment(), "Communication Fragment"),
        Triple(R.id.tab_profile, ProfileFragment(), "Profile Fragment")
    )
)
