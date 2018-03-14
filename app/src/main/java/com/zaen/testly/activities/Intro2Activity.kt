package com.zaen.testly.activities

import com.stephentuso.welcome.BasicPage
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeConfiguration
import com.zaen.testly.R

/**
 * Created by zaen on 3/10/18.
 */
class Intro2Activity : WelcomeActivity() {
    override fun configuration(): WelcomeConfiguration {
        return WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat.ttf")
                .defaultHeaderTypefacePath("Montserrat.ttf")
                .page(BasicPage(R.drawable.ic_search,getString(R.string.intro_first_title),getString(R.string.intro_first_description)))
                .canSkip(true)
                .backButtonSkips(true)
                .backButtonNavigatesPages(true)
                .swipeToDismiss(true)
                .bottomLayout(WelcomeConfiguration.BottomLayout.STANDARD)
                .exitAnimation(android.R.anim.fade_out)
                .build()
    }
}