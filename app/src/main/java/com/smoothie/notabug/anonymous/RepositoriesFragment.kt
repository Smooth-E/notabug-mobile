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

        val adapter = CodeRecyclerViewAdapter(activity as Activity, data)
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
                    val header = element.getElementsByClass("ui header")[0]
                    val name = element.getElementsByClass("name")[0]
                    val stats = header.getElementsByClass("ui right metas")[0].getElementsByTag("span")
                    val possibleDescription = element.getElementsByClass("has-emoji")
                    val holder = CodeRecyclerViewAdapter.DataHolder(
                        url = name.attr("href"),
                        icon = R.drawable.ic_baseline_class_24,
                        name = name.text(),
                        description = if (possibleDescription.size > 0) possibleDescription[0].text() else "",
                        modificationDate = element.getElementsByClass("time")[0].text(),
                        stars = stats[0].text().toInt(),
                        forks = stats[1].text().toInt()
                    )
                    data.add(holder)
                }
                val adapter = CodeRecyclerViewAdapter(activity, data)
                recyclerView.adapter = adapter
            }
        }
    }

}
