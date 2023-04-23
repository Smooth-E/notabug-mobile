package com.smoothie.notabug.repository

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.smoothie.notabug.R
import com.smoothie.notabug.Utilities
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.InterruptedIOException

abstract class LoadableFragment(private val url: String, private val layoutResource: Int)
    : Fragment(R.layout.fragment_loadable) {

    private lateinit var viewGroupErrorLoading: ViewGroup
    private lateinit var buttonRetry: Button
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    protected lateinit var inflatedView: View

    abstract fun parsePage(document: Document)

    private fun reloadPage() {
        Thread {
            try {
                requireActivity().runOnUiThread {
                    progressIndicator.visibility = View.VISIBLE
                    swipeRefreshLayout.visibility = View.INVISIBLE
                    viewGroupErrorLoading.visibility = View.INVISIBLE
                }
                val page = Utilities.get(url)
                requireActivity().runOnUiThread {
                    parsePage(Jsoup.parse(page))
                    progressIndicator.visibility = View.INVISIBLE
                    viewGroupErrorLoading.visibility = View.INVISIBLE
                    swipeRefreshLayout.visibility = View.VISIBLE
                }
            }
            catch (exception: Exception) {
                if (exception is InterruptedIOException ||
                        exception is InterruptedException ||
                        exception is java.lang.IllegalStateException)
                    return@Thread
                Log.w("reloadPage", exception.toString())
                requireActivity().runOnUiThread {
                    viewGroupErrorLoading.visibility = View.VISIBLE
                    progressIndicator.visibility = View.INVISIBLE
                    swipeRefreshLayout.visibility = View.INVISIBLE
                }
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewGroupErrorLoading = view.findViewById(R.id.group_loading_error)
        buttonRetry = view.findViewById(R.id.button_try_again)
        progressIndicator = view.findViewById(R.id.progress_indicator)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nested_scroll_view)
        inflatedView = layoutInflater.inflate(layoutResource, nestedScrollView, true)

        buttonRetry.setOnClickListener { reloadPage() }

        swipeRefreshLayout.setOnRefreshListener { reloadPage() }

        reloadPage()

    }

}