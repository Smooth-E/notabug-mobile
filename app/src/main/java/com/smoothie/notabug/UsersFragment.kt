package com.smoothie.notabug

import com.smoothie.notabug.explore.PeopleRecyclerViewFragment

class UsersFragment(searchQuery: String) : PeopleRecyclerViewFragment(searchQuery) {
    override val connectionUrl = "https://notabug.org/explore/users"
}
