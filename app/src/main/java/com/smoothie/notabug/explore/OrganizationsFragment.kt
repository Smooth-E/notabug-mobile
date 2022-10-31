package com.smoothie.notabug.explore

class OrganizationsFragment(searchQuery: String) : PeopleRecyclerViewFragment(searchQuery) {
    override val connectionUrl = "https://notabug.org/explore/organizations"
}
