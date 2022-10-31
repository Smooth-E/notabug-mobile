package com.smoothie.notabug

import com.smoothie.notabug.explore.CodeFragment
import com.smoothie.notabug.explore.PeopleFragment

class AnonymousHubActivity() : HubActivity(
    R.menu.bottom_navigation_anonymous,
    arrayOf(
        Triple(R.id.tab_code, CodeFragment("search phrasee"), "Code Fragment"),
        Triple(R.id.tab_people, PeopleFragment("search phrase"), "People Fragment"),
        Triple(R.id.tab_settings, SettingsFragment(), "Settings Fragment")
    )
)
