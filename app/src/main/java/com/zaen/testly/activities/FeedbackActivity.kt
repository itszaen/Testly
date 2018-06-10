package com.zaen.testly.activities

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.utils.ScreenPropUtils
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.form_feedback.*


class FeedbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_feedback
        super.onCreate(savedInstanceState)
        form_feedback.y = (ScreenPropUtils(this).getHeight() - 48).toFloat()
        form_feedback.bringToFront()
        btn_submit_feedback.setIconResource(IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_send)
                .color(ContextCompat.getColor(this,R.color.md_grey_600))
                .sizeDp(32))
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        toolbar?.setDisplayShowTitleEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Up button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities()
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }

        // ...

        }
        return super.onOptionsItemSelected(item)
    }

    fun onNewFeedback(view: View){
        val slideUp = AnimationUtils.loadAnimation(applicationContext,
                R.anim.slide_up)
        slideUp.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                content_activity_feedback.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        content_activity_feedback.startAnimation(slideUp)
        form_feedback.startAnimation(slideUp)
    }

    fun onSendClick(view: View){

    }
}
