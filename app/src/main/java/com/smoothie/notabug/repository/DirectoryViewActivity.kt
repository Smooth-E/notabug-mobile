package com.smoothie.notabug.repository

import kotlin.reflect.KClass

class DirectoryViewActivity : LoadablePageActivity<DirectoryViewFragment>() {

    override fun createParserFragment(page: String): DirectoryViewFragment =
        DirectoryViewFragment(page)

    override fun getActivityClass(): KClass<*> = DirectoryViewActivity::class

}
