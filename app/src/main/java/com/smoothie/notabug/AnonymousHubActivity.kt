package com.smoothie.notabug

import com.smoothie.notabug.explore.CodeFragment
import com.smoothie.notabug.explore.PeopleFragment

class AnonymousHubActivity() : HubActivity(
    R.menu.bottom_navigation_anonymous,
    arrayOf(
        Triple(R.id.tab_code, CodeFragment(), "Code Fragment"),
        Triple(R.id.tab_people, PeopleFragment(), "People Fragment"),
        Triple(R.id.tab_settings, SettingsFragment(), "Settings Fragment")
    )
)
