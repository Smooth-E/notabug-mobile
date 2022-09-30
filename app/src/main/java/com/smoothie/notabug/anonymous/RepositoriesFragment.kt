package com.smoothie.notabug.anonymous

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.Debug
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smoothie.notabug.FadingFragment
import com.smoothie.notabug.R
import org.jsoup.Jsoup

class RepositoriesFragment : FadingFragment(R.layout.fragment_anonymous_repositories) {

    private lateinit var recyclerView: RecyclerView

    private var data: ArrayList<CodeRecyclerViewAdapter.DataHolder> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.view_root)

        val adapter = CodeRecyclerViewAdapter(data)
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        LoadingThread(activity).start()
    }

    inner class LoadingThread(private val activity: Activity?) : Thread() {
        override fun run() {
            val document = Jsoup.connect("https://notabug.org/explore/repos").get()
            val repositories = document.body().getElementsByClass("ui repository list")[0]
            activity?.runOnUiThread {
                for (element in repositories.getElementsByClass("item")) {
                    val header = element.getElementsByClass("name")[0]
                    val holder = CodeRecyclerViewAdapter.DataHolder(
                        url = header.attr("href"),
                        icon = R.drawable.ic_baseline_class_24,
                        name = header.text(),
                        description = "Hello",
                        modificationDate = "",
                        stars = 123,
                        forks = 123
                    )
                    data.add(holder)
                }
                val adapter = CodeRecyclerViewAdapter(data)
                recyclerView.adapter = adapter
            }
        }
    }

}
