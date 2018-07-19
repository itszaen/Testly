package com.zaen.testly.intro.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MotionEvent
import com.stephentuso.welcome.BasicPage
import com.stephentuso.welcome.FragmentWelcomePage
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeConfiguration
import com.zaen.testly.R
import com.zaen.testly.auth.activities.AuthActivity.Companion.RC_LOG_IN
import com.zaen.testly.auth.activities.AuthActivity.Companion.RC_SIGN_UP
import com.zaen.testly.auth.activities.AuthActivity.Companion.RC_SIGN_UP_INFO
import com.zaen.testly.auth.activities.LoginActivity
import com.zaen.testly.auth.activities.SignupActivity
import com.zaen.testly.auth.activities.SignupInfoActivity
import com.zaen.testly.intro.fragments.FaqFragment
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.SupportActivityDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator



/**
 * Created by zaen on 3/9/18.
 */
class IntroActivity : WelcomeActivity(), ISupportActivity {
    private val mDelegate = SupportActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

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


    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) { mDelegate.fragmentAnimator = fragmentAnimator }

    override fun getFragmentAnimator(): FragmentAnimator { return mDelegate.fragmentAnimator }

    override fun onBackPressedSupport() { mDelegate.onBackPressedSupport() }

    override fun extraTransaction(): ExtraTransaction { return mDelegate.extraTransaction() }

    override fun onCreateFragmentAnimator(): FragmentAnimator { return mDelegate.onCreateFragmentAnimator() }

    override fun getSupportDelegate(): SupportActivityDelegate { return mDelegate }

    override fun post(runnable: Runnable?) { mDelegate.post(runnable) }

}