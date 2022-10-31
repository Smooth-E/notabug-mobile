package com.smoothie.notabug.explore

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.smoothie.notabug.R
import com.smoothie.notabug.RecyclerViewWithFooterAdapter
import com.smoothie.notabug.ScrollerRecyclerViewFragment
import com.smoothie.notabug.Utilities
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.InterruptedIOException
import java.net.URL

open class PeopleRecyclerViewFragment(searchQuery: String) :
    ScrollerRecyclerViewFragment<PeopleRecyclerViewAdapter, PeopleRecyclerViewAdapter.DataHolder>(20, searchQuery) {

    override fun createAdapter() = PeopleRecyclerViewAdapter(this, data)

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
                val document = Jsoup.parse(Utilities.get("${connectionUrl}?page=${pageNumber}&q=${searchQuery}"))
                val list = document.getElementsByClass("ui user list")[0]
                val userElements = list.getElementsByClass("item")
                if (userElements.size < 20) (recyclerView.adapter as RecyclerViewWithFooterAdapter<*, *, *>).stopLoadingNewPages()
                for (user in userElements) {
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
                    icons.removeAll { it.className() == "octicon octicon-link" || it.className() == "octicon octicon-mail" }
                    val urlElements = description.getElementsByTag("a")
                    for (element in urlElements) {
                        if (element.attr("href").startsWith("mailto:")) email = element.text()
                        else website = element.text()
                    }
                    for (index in textNodes.indices) {
                        val text = textNodes[index].text().trim()
                        when(val className = icons[index].className()) {
                            "octicon octicon-mail" -> email = text
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
                    }
                }
                activity?.runOnUiThread {
                    if (data.size > 0) {
                        recyclerView.adapter?.notifyItemRangeInserted(pageNumber * 20 - 20, userElements.size)
                        recyclerView.visibility = View.VISIBLE
                        nothingFoundWarning.visibility = View.GONE
                        if (userElements.size < 20) recyclerView.children.elementAt(recyclerView.childCount - 1).visibility = View.GONE
                    }
                    else {
                        recyclerView.visibility = View.GONE
                        nothingFoundWarning.visibility = View.VISIBLE
                    }
                    if (pageNumber <= 1) {
                        // Scroll manually or else recycler scrolls to the bottom automatically
                        recyclerView.layoutManager?.scrollToPosition(0)
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
