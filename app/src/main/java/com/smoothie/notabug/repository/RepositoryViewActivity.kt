package com.smoothie.notabug.repository

import kotlin.reflect.KClass

class RepositoryViewActivity : LoadablePageActivity<RepositoryViewFragment>() {

    override fun createParserFragment(page: String): RepositoryViewFragment =
        RepositoryViewFragment(page)

    override fun getActivityClass(): KClass<*> = RepositoryViewActivity::class

}
