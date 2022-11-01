package com.smoothie.notabug.authorized

import com.smoothie.notabug.HubActivity
import com.smoothie.notabug.R

class AuthorizedHubActivity : HubActivity(
    R.menu.bottom_navigation_user,
    arrayOf(
        Triple(R.id.tab_explore, ExplorePagerFragment(), "Explore Fragment"),
        Triple(R.id.tab_communication, CommunicationPagerFragment(), "Communication Fragment"),
        Triple(R.id.tab_profile, ProfileFragment(), "Profile Fragment")
    )
)
