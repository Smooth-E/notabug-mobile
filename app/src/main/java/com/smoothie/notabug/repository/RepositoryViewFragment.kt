package com.smoothie.notabug.repository

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.google.android.material.elevation.SurfaceColors
import com.smoothie.notabug.R

class RepositoryViewFragment(page: String) : PageParserFragment(page, R.layout.fragment_repository_view) {

    private var topAppBarColorAnimation: ValueAnimator = ValueAnimator()
    private lateinit var viewGroupTopAppBar: ViewGroup
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var viewRepositoryDescription: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewGroupTopAppBar = loadableActivity.findViewById(R.id.top_app_bar)
        nestedScrollView = loadableActivity.findViewById(R.id.nested_scroll_view)
        viewRepositoryDescription = loadableActivity.findViewById(R.id.repository_description)

        viewGroupTopAppBar.setPadding(
            viewGroupTopAppBar.paddingLeft,
            viewGroupTopAppBar.top + loadableActivity.systemBarInsets().top,
            viewGroupTopAppBar.paddingRight,
            viewGroupTopAppBar.paddingBottom
        )

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
    }

}