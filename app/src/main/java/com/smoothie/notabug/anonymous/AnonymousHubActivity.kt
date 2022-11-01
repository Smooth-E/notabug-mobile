package com.smoothie.notabug.anonymous

import com.smoothie.notabug.HubActivity
import com.smoothie.notabug.R
import com.smoothie.notabug.SettingsFragment

class AnonymousHubActivity() : HubActivity(
    R.menu.bottom_navigation_anonymous,
    arrayOf(
        Triple(R.id.tab_code, CodeFragment(), "Code Fragment"),
        Triple(R.id.tab_people, PeopleFragment(), "People Fragment"),
        Triple(R.id.tab_settings, SettingsFragment(), "Settings Fragment")
    )
)
