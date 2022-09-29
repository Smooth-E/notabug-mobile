package com.smoothie.notabug.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.Debug
import com.smoothie.notabug.FadingFragment
import com.smoothie.notabug.R
import org.jsoup.Jsoup

class RepositoriesFragment : FadingFragment(R.layout.fragment_anonymous_repositories) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadingThread().start()
    }

    inner class LoadingThread : Thread() {
        override fun run() {
            val document = Jsoup.connect("https://notabug.org/explore/repos").get()
            val repositories = document.body().getElementsByClass("ui repository list")[0]
            for (element in repositories.getElementsByClass("item")) {
                val header = element.getElementsByClass("name")[0]
                val name = header.text()
                val url = header.attr("href")
                Log.d(
                    "REPOSITORIES",
                    "Repository\n" +
                            "name: $name\n" +
                            "url: $url\n" +
                            "----------------\n\n"
                )
            }
        }
    }

}
