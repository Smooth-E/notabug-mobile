package com.smoothie.notabug

import com.smoothie.notabug.explore.CodeFragment
import com.smoothie.notabug.explore.PeopleFragment

class AnonymousHubActivity : HubActivity(R.menu.bottom_navigation_anonymous, R.id.tab_code) {
    override fun getFragment(itemId: Int): FadingFragment {
        return when (itemId) {
            R.id.tab_code -> CodeFragment()
            R.id.tab_people -> PeopleFragment()
            R.id.tab_settings -> SettingsFragment()
            else -> throw IllegalAccessError("Unexpected item ID: $itemId")
        }
    }
}
