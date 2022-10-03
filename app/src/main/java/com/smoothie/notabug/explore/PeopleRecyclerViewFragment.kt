package com.smoothie.notabug.explore

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.smoothie.notabug.R
import com.smoothie.notabug.ScrollerRecyclerViewFragment
import com.smoothie.notabug.Utilities
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.InterruptedIOException
import java.net.URL

open class PeopleRecyclerViewFragment : ScrollerRecyclerViewFragment<PeopleRecyclerViewAdapter, PeopleRecyclerViewAdapter.DataHolder>() {

    protected open val connectionUrl = "https://notabug.org"

    override fun getAdapter() = PeopleRecyclerViewAdapter(this, data)

    override fun loadNewPage(isReloading: Boolean) = LoadingThread(isReloading).start()

    override fun onDestroyView() {
        super.onDestroyView()
        thread?.interrupt()
    }

    inner class LoadingThread(private val isReloading: Boolean = false) : Thread() {
        override fun run() {
            try {
                thread?.join()
                thread = this
                if (isReloading) {
                    val size = data.size
                    activity?.runOnUiThread {
                        for (index in 0 until size) data.removeFirst()
                        recyclerView.adapter?.notifyItemRangeRemoved(0, size)
                    }
                }
                pageNumber++
                val document = Jsoup.parse(Utilities.get("${connectionUrl}?page=${pageNumber}"))
                val list = document.getElementsByClass("ui user list")[0]
                for (user in list.getElementsByClass("item")) {
                    var imageUrl = user.getElementsByClass("ui avatar image")[0].attr("src")
                    if (imageUrl.startsWith("/")) imageUrl = "https://notabug.org$imageUrl"
                    val profilePicture =
                        try {
                            val bitmap = BitmapFactory.decodeStream(URL(imageUrl).openStream())
                            BitmapDrawable(resources, bitmap)
                        }
                        catch (exception: InterruptedIOException) {
                            Log.d("TAG", "Thread interrupted, passing exception up the stack")
                            throw exception
                        }
                        catch (exception: Exception) {
                            Log.e("PeopleRVF","Failed to download user avatar from $imageUrl with the following exception:"
                            )
                            exception.printStackTrace()
                            ResourcesCompat.getDrawable(resources,
                                R.drawable.ic_baseline_person_24, context?.theme)
                        }
                    val content = user.getElementsByClass("content")[0]
                    val header = content.getElementsByClass("header")[0]
                    val username = header.getElementsByTag("a")[0].text()
                    val fullName = header.text().removePrefix(username).trim()
                    val description = content.getElementsByClass("description")[0]
                    val textNodes = ArrayList<TextNode>(description.textNodes().drop(1))
                    val icons = description.getElementsByTag("i")
                    var email = ""
                    var website = ""
                    var location = ""
                    var joinDate = ""
                    textNodes.removeAll { it.isBlank }
                    icons.removeAll { it.className() == "octicon octicon-link" }
                    val possiblyWebsite = description.getElementsByTag("a")
                    if (possiblyWebsite.size > 0) website = possiblyWebsite[0].attr("href")
                    for (index in textNodes.indices) {
                        val text = textNodes[index].text().trim()
                        when(val className = icons[index].className()) {
                            "octicon octicon-email" -> email = text
                            "octicon octicon-location" -> location = text
                            "octicon octicon-clock" -> joinDate = text
                            else -> throw IllegalArgumentException("Unexpected icon class: $className")
                        }
                    }
                    activity?.runOnUiThread {
                        data.add(
                            PeopleRecyclerViewAdapter.DataHolder(
                                profilePicture = profilePicture,
                                username = username,
                                fullName = fullName,
                                email = email,
                                website = website,
                                location = location,
                                joinDate = joinDate
                            )
                        )
                        recyclerView.adapter?.notifyItemInserted(data.size - 1)
                    }
                }
                if (pageNumber <= 1) {
                    activity?.runOnUiThread {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
            catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}
