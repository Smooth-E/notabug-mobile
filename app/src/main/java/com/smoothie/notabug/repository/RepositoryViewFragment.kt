package com.smoothie.notabug.repository

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R
import org.jsoup.Jsoup

class RepositoryViewFragment(private val page: String) : PageParserFragment(page, R.layout.fragment_repository_view) {

    private var topAppBarColorAnimation: ValueAnimator = ValueAnimator()
    private lateinit var viewGroupTopAppBar: ViewGroup
    private lateinit var buttonBack: ImageView
    private lateinit var imageViewRepositoryIcon: ImageView
    private lateinit var textViewRepositoryName: TextView
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var textViewRepositoryDescription: TextView
    private lateinit var buttonRepositoryMirror: View
    private lateinit var textViewRepositoryMirror: TextView
    private lateinit var buttonStar: Button
    private lateinit var buttonWatch: Button
    private lateinit var buttonFork: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val document = Jsoup.parse(page)

        viewGroupTopAppBar = loadableActivity.findViewById(R.id.top_app_bar)
        buttonBack = loadableActivity.findViewById(R.id.button_back)
        imageViewRepositoryIcon = loadableActivity.findViewById(R.id.repository_icon)
        textViewRepositoryName = loadableActivity.findViewById(R.id.repository_name)
        nestedScrollView = loadableActivity.findViewById(R.id.nested_scroll_view)
        textViewRepositoryDescription = loadableActivity.findViewById(R.id.repository_description)
        buttonRepositoryMirror = loadableActivity.findViewById(R.id.group_mirror_info)
        textViewRepositoryMirror = loadableActivity.findViewById(R.id.repository_mirror_link)
        buttonStar = loadableActivity.findViewById(R.id.button_star)
        buttonWatch = loadableActivity.findViewById(R.id.button_watch)
        buttonFork = loadableActivity.findViewById(R.id.button_fork)

        viewGroupTopAppBar.setPadding(
            viewGroupTopAppBar.paddingLeft,
            viewGroupTopAppBar.top + loadableActivity.systemBarInsets().top,
            viewGroupTopAppBar.paddingRight,
            viewGroupTopAppBar.paddingBottom
        )

        buttonBack.setOnClickListener { loadableActivity.finish() }

        var icon = R.drawable.ic_baseline_class_24
        if (document.select(".mega-octicon.octicon-repo-clone").size > 0)
            icon = R.drawable.ic_baseline_collections_bookmark_24
        else if (document.select(".mega-octicon.octicon-lock").size > 0)
            icon = R.drawable.ic_baseline_lock_24
        imageViewRepositoryIcon.setImageResource(icon)

        val header = document.select("div.ui.header")[0]
        val breadcrumb = header.select("div.ui.huge.breadcrumb")[0]

        textViewRepositoryName.text = breadcrumb.select("a")[1].text().trim()

        val elevatedColor = SurfaceColors.getColorForElevation(loadableActivity, 8f)
        val topAppBarBackgroundColor = (viewGroupTopAppBar.background as ColorDrawable).color
        val animationDuration = loadableActivity.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        nestedScrollView.setOnScrollChangeListener(object:NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                if (oldScrollY <= 0 && scrollY > 0) {
                    topAppBarColorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), topAppBarBackgroundColor, elevatedColor)
                    topAppBarColorAnimation.duration = animationDuration
                    topAppBarColorAnimation.addUpdateListener { viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int) }
                    topAppBarColorAnimation.start()
                }
                else if (oldScrollY > 0 && scrollY <= 0) {
                    topAppBarColorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), elevatedColor,  topAppBarBackgroundColor)
                    topAppBarColorAnimation.duration = animationDuration
                    topAppBarColorAnimation.addUpdateListener { viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int) }
                    topAppBarColorAnimation.start()
                }
            }
        })

        val possiblyDescription = document.select("span.description.has-emoji")
        if (possiblyDescription.size == 0) textViewRepositoryDescription.visibility = View.GONE
        else textViewRepositoryDescription.text = possiblyDescription[0].text().trim()

        val possiblyForkFlag = header.getElementsByClass("fork-flag")
        if (possiblyForkFlag.size == 0) {
            buttonRepositoryMirror.visibility = View.GONE
        }
        else {
            val forkFlag = possiblyForkFlag[0].getElementsByTag("a")
            textViewRepositoryMirror.text = forkFlag.text().trim()
            buttonRepositoryMirror.setOnClickListener {
                val url = forkFlag.attr("href")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        val rightUI = header.select("div.ui.right")[0].select("div.ui.labeled.button")
        buttonStar.text = rightUI[1].select("a.ui.basic.label")[0].text().trim()
        buttonWatch.text = rightUI[0].select("a.ui.basic.label")[0].text().trim()
        buttonFork.text = rightUI[2].select("a.ui.basic.label")[0].text().trim()
    }

}
