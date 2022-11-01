package com.smoothie.notabug.repository

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.smoothie.notabug.LimitlessActivity
import com.smoothie.notabug.R
import com.smoothie.notabug.Utilities
import kotlin.reflect.KClass

abstract class LoadablePageActivity<ParserFragmentType: PageParserFragment> : LimitlessActivity() {

    companion object {
        const val EXTRA_NAME_URL = "PAGE_URL"
    }

    private lateinit var progressBarLoadingSpinner: ProgressBar
    private lateinit var viewGroupErrorLoading: ViewGroup
    private lateinit var buttonTryAgain: Button
    private lateinit var buttonGoBack: Button

    protected abstract fun createParserFragment(page: String): ParserFragmentType

    protected abstract fun getActivityClass(): KClass<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadable_page)

        progressBarLoadingSpinner = findViewById(R.id.loading_spinner)
        viewGroupErrorLoading = findViewById(R.id.group_loading_error)
        buttonTryAgain = findViewById(R.id.button_try_again)
        buttonGoBack = findViewById(R.id.button_go_back)

        val url = intent.getStringExtra(EXTRA_NAME_URL)
        if (url.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.label_no_url), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        buttonTryAgain.setOnClickListener {
            val intent = Intent(this, getActivityClass().java)
            intent.putExtra(EXTRA_NAME_URL, url)
            startActivity(intent)
            finish()
        }

        buttonGoBack.setOnClickListener { finish() }

        progressBarLoadingSpinner.visibility = View.VISIBLE
        viewGroupErrorLoading.visibility = View.INVISIBLE

        Thread {
            try {
                val page = Utilities.get(url, false)
                runOnUiThread {
                    progressBarLoadingSpinner.visibility = View.INVISIBLE
                    viewGroupErrorLoading.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.view_root, createParserFragment(page))
                        .commit()
                }
            }
            catch (exception: Exception) {
                Log.w("LoadablePageActivity", "Error in Loading Thread: $exception")
                runOnUiThread {
                    progressBarLoadingSpinner.visibility = View.INVISIBLE
                    viewGroupErrorLoading.visibility = View.VISIBLE
                }
            }
        }.start()
    }

}