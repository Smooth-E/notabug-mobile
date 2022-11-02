package com.smoothie.notabug.repository

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.smoothie.notabug.R
import com.smoothie.notabug.Utilities
import org.jsoup.Jsoup
import java.io.InterruptedIOException
import java.net.URL
import java.util.concurrent.TimeoutException

class RepositoryViewFragment(private val page: String)
    : PageParserFragment(page, R.layout.fragment_repository_view) {

    private var topAppBarColorAnimation: ValueAnimator = ValueAnimator()
    private lateinit var avatarLoadingThread: Thread
    private lateinit var viewRoot: View
    private lateinit var viewGroupTopAppBar: ViewGroup
    private lateinit var buttonBack: ImageView
    private lateinit var imageViewRepositoryIcon: ImageView
    private lateinit var textViewRepositoryName: TextView
    private lateinit var tabLayoutAlternative: TabLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var textViewRepositoryDescription: TextView
    private lateinit var buttonAuthor: View
    private lateinit var progressBarAuthorAvatar: ProgressBar
    private lateinit var imageViewAuthorAvatar: ShapeableImageView
    private lateinit var textViewAuthorName: TextView
    private lateinit var buttonRepositoryMirror: View
    private lateinit var textViewRepositoryMirror: TextView
    private lateinit var linearLayoutActionButtonsParent: LinearLayout
    private lateinit var buttonStar: Button
    private lateinit var buttonWatch: Button
    private lateinit var buttonFork: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2

    private fun initializeTabLayout(tabLayout: TabLayout, tabLayoutTwin: TabLayout) {
        tabLayout.addOnTabSelectedListener(object:OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tabLayout.visibility != View.VISIBLE) return
                tabLayoutTwin.selectTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        tabLayout.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (tabLayout.visibility != View.VISIBLE) return@setOnScrollChangeListener
            tabLayoutTwin.scrollY = scrollY
            tabLayoutTwin.scrollX = scrollX
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val document = Jsoup.parse(page)

        viewRoot = loadableActivity.findViewById(R.id.view_root)
        viewGroupTopAppBar = loadableActivity.findViewById(R.id.top_app_bar)
        buttonBack = loadableActivity.findViewById(R.id.button_back)
        imageViewRepositoryIcon = loadableActivity.findViewById(R.id.repository_icon)
        textViewRepositoryName = loadableActivity.findViewById(R.id.repository_name)
        tabLayoutAlternative = loadableActivity.findViewById(R.id.view_tab_layout_alternative)
        nestedScrollView = loadableActivity.findViewById(R.id.nested_scroll_view)
        textViewRepositoryDescription = loadableActivity.findViewById(R.id.repository_description)
        buttonAuthor = loadableActivity.findViewById(R.id.group_author_info)
        progressBarAuthorAvatar = loadableActivity.findViewById(R.id.author_avatar_progress_spinner)
        imageViewAuthorAvatar = loadableActivity.findViewById(R.id.author_avatar)
        textViewAuthorName = loadableActivity.findViewById(R.id.author_name)
        buttonRepositoryMirror = loadableActivity.findViewById(R.id.group_mirror_info)
        textViewRepositoryMirror = loadableActivity.findViewById(R.id.repository_mirror_link)
        linearLayoutActionButtonsParent = loadableActivity.findViewById(R.id.action_buttons_parent)
        buttonStar = loadableActivity.findViewById(R.id.button_star)
        buttonWatch = loadableActivity.findViewById(R.id.button_watch)
        buttonFork = loadableActivity.findViewById(R.id.button_fork)
        tabLayout = loadableActivity.findViewById(R.id.view_tab_layout)
        viewPager2 = loadableActivity.findViewById(R.id.view_pager2)

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

        val header = document.getElementsByClass("header-wrapper")[0]
            .getElementsByClass("header")[0]
        val breadcrumb = document.getElementsByClass("breadcrumb")[0]

        val delay = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        textViewRepositoryName.ellipsize = TextUtils.TruncateAt.MARQUEE;
        textViewRepositoryName.text = breadcrumb.select("a")[1].text().trim()
        Handler(Looper.getMainLooper()).postDelayed({
            textViewRepositoryName.isSelected = true
        }, delay)

        val possiblyDescription = document.select("span.description.has-emoji")
        if (possiblyDescription.size == 0) textViewRepositoryDescription.visibility = View.GONE
        else textViewRepositoryDescription.text = possiblyDescription[0].text().trim()

        val authorElement = breadcrumb.getElementsByTag("a")[0]
        textViewAuthorName.text = authorElement.text().trim()
        val authorProfileUrl = "https://notabug.org" + authorElement.attr("href")
        imageViewAuthorAvatar.visibility = View.INVISIBLE
        progressBarAuthorAvatar.visibility = View.VISIBLE
        avatarLoadingThread = Thread {
            while (true) {
                try {
                    val page = Jsoup.parse(Utilities.get(authorProfileUrl))
                    val possiblyOrganizationAvatar = page.getElementById("org-avatar")
                    var avatarUrl = if (possiblyOrganizationAvatar != null) {
                        possiblyOrganizationAvatar.attr("src")
                    }
                    else {
                        page.select(".user.profile")[0]
                            .getElementsByClass("card")[0]
                            .getElementsByClass("image")[0]
                            .getElementsByTag("img")[0]
                            .attr("src")
                    }
                    if (avatarUrl.startsWith("/")) avatarUrl = "https://notabug.org$avatarUrl"
                    val avatar = BitmapFactory.decodeStream(URL(avatarUrl).openStream())
                    val avatarDrawable = BitmapDrawable(resources, avatar)
                    loadableActivity.runOnUiThread {
                        imageViewAuthorAvatar.setImageDrawable(avatarDrawable)
                        imageViewAuthorAvatar.visibility = View.VISIBLE
                        progressBarAuthorAvatar.visibility = View.INVISIBLE
                    }
                    break
                }
                catch (exception : Exception) {
                    if (exception is TimeoutException)
                        Log.d("Author Avatar", "Safe exception occured.\n$exception")
                    else if (exception is InterruptedException ||
                        exception is InterruptedIOException ||
                        exception is IllegalStateException)
                        break
                    else throw exception
                }
            }
        }
        avatarLoadingThread.start()

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

        val rightUI = header.getElementsByClass("right")[0]
            .select("div.ui.labeled.button")
        buttonStar.text = rightUI[1].select("a.ui.basic.label")[0].text().trim()
        buttonWatch.text = rightUI[0].select("a.ui.basic.label")[0].text().trim()
        if (rightUI.size == 3) {
            buttonFork.text = rightUI[2].select("a.ui.basic.label")[0].text().trim()
        }
        else {
            buttonFork.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0f
            )
            buttonFork.visibility = View.GONE
            linearLayoutActionButtonsParent.weightSum = 2f
        }

        initializeTabLayout(tabLayout, tabLayoutAlternative)
        initializeTabLayout(tabLayoutAlternative, tabLayout)

        val elevatedColor = SurfaceColors.getColorForElevation(loadableActivity, 8f)
        val topAppBarBackgroundColor = (viewGroupTopAppBar.background as ColorDrawable).color
        val animationDuration =
            resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        tabLayoutAlternative.background = ColorDrawable(elevatedColor)

        nestedScrollView.post {
            val layoutParams = viewPager2.layoutParams
            layoutParams.height = view.measuredHeight - tabLayout.measuredHeight
            viewPager2.layoutParams = layoutParams

            val difference = nestedScrollView.measuredHeight - viewPager2.measuredHeight - tabLayout.measuredHeight

            nestedScrollView.setOnScrollChangeListener(OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (oldScrollY <= 0 && scrollY > 0) {
                    topAppBarColorAnimation =
                        ValueAnimator.ofObject(ArgbEvaluator(), topAppBarBackgroundColor, elevatedColor)
                    topAppBarColorAnimation.duration = animationDuration
                    topAppBarColorAnimation.addUpdateListener {
                        viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int)
                    }
                    topAppBarColorAnimation.start()
                }
                else if (oldScrollY > 0 && scrollY <= 0) {
                    topAppBarColorAnimation =
                        ValueAnimator.ofObject(ArgbEvaluator(), elevatedColor, topAppBarBackgroundColor)
                    topAppBarColorAnimation.duration = animationDuration
                    topAppBarColorAnimation.addUpdateListener {
                        viewGroupTopAppBar.background = ColorDrawable(it.animatedValue as Int)
                    }
                    topAppBarColorAnimation.start()
                }

                if (difference in (oldScrollY + 1)..scrollY) {
                    tabLayout.visibility = View.INVISIBLE
                    tabLayoutAlternative.visibility = View.VISIBLE
                }
                else if (difference in (scrollY + 1)..oldScrollY) {
                    tabLayout.visibility = View.VISIBLE
                    tabLayoutAlternative.visibility = View.GONE
                }
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        avatarLoadingThread.interrupt()
    }

}
