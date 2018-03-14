package com.zaen.testly.activities

import android.content.Intent
import android.support.v4.app.Fragment
import com.stephentuso.welcome.*
import com.zaen.testly.R
import com.zaen.testly.activities.auth.LoginActivity
import com.zaen.testly.activities.auth.SignupActivity
import com.zaen.testly.fragments.intro.FaqFragment
import com.stephentuso.welcome.WelcomeActivity

/**
 * Created by zaen on 3/9/18.
 */
class IntroActivity : WelcomeActivity() {
    companion object {
        val RC_LOG_IN = 101
        val RC_SIGN_UP = 102
    }
    override fun configuration():WelcomeConfiguration {
        return WelcomeConfiguration.Builder(this)
                .page(BasicPage(R.drawable.ic_search, getString(R.string.intro_first_title), getString(R.string.intro_first_description))
                        .background(R.color.introFirstBackground))
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
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode){
            RC_LOG_IN -> {
                when (resultCode){
                    RESULT_OK -> {setResult(RESULT_OK,intent);finish()
                        //completeWelcomeScreen();return
                        }
                    RC_SIGN_UP -> onButtonBarSecondPressed()
                }
            }
            RC_SIGN_UP -> {
                when (resultCode){
                    RESULT_OK -> {setResult(RESULT_OK,intent);finish()
                        //completeWelcomeScreen();return
                    }
                }
            }
        }
    }

}