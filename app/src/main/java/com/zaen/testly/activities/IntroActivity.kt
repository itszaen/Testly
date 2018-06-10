package com.zaen.testly.activities

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.stephentuso.welcome.BasicPage
import com.stephentuso.welcome.FragmentWelcomePage
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeConfiguration
import com.zaen.testly.R
import com.zaen.testly.activities.auth.AuthActivity.Companion.RC_LOG_IN
import com.zaen.testly.activities.auth.AuthActivity.Companion.RC_SIGN_UP
import com.zaen.testly.activities.auth.AuthActivity.Companion.RC_SIGN_UP_INFO
import com.zaen.testly.activities.auth.LoginActivity
import com.zaen.testly.activities.auth.SignupActivity
import com.zaen.testly.activities.auth.SignupInfoActivity
import com.zaen.testly.fragments.intro.FaqFragment

/**
 * Created by zaen on 3/9/18.
 */
class IntroActivity : WelcomeActivity() {
    override fun configuration() : WelcomeConfiguration {
        return WelcomeConfiguration.Builder(this)
                // Page 1
                .page(BasicPage(R.drawable.ic_search, getString(R.string.intro_first_title), getString(R.string.intro_first_description))
                        .background(R.color.introFirstBackground))
                // Page 2
                .page(object:FragmentWelcomePage() {
                    override fun fragment(): Fragment {
                    return FaqFragment()
                    }}
                        .background(R.color.introSecondBackground)
                )
                .canSkip(false)
                .swipeToDismiss(false)
                .backButtonNavigatesPages(true)
                .bottomLayout(WelcomeConfiguration.BottomLayout.BUTTON_BAR)
                .exitAnimation(android.R.anim.fade_out)
                .build()
    }

    override fun onButtonBarFirstPressed() {
        super.onButtonBarFirstPressed()
        startActivityForResult(Intent(this, LoginActivity::class.java),RC_LOG_IN)
    }

    override fun onButtonBarSecondPressed() {
        super.onButtonBarSecondPressed()
        startActivityForResult(Intent(this, SignupActivity::class.java),RC_SIGN_UP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode){
            RC_LOG_IN -> {
                when (resultCode){
                    RESULT_OK -> {
                        setResult(Activity.RESULT_OK,intent)
                        // do NOT use completeWelcomeScreen()
                        finish()
                        return
                        }
                    RC_SIGN_UP -> onButtonBarSecondPressed()
                    RC_SIGN_UP_INFO -> {
                        startActivityForResult(Intent(this, SignupInfoActivity::class.java), RC_SIGN_UP)
                    }
                }
            }
            RC_SIGN_UP -> {
                when (resultCode) {
                    RESULT_OK -> {
                        //completeWelcomeScreen()
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                        return
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun welcomeKey():String{
        return resources.getString(R.string.intro_key_unique)
    }

}