package com.zaen.testly.activities

import android.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.zaen.testly.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_action_close)

        val aboutPage = AboutPage(this)
                .setDescription("a")
                .isRTL(false)
                .setImage(R.drawable.kaisei)
                .addItem(Element().setTitle("Version 1.0"))
                .addGroup("Contact")
                .addEmail("chouzaen2002@gmail.com")
                .addTwitter("zaen200233")
                //.addPlayStore("com.")
                .addGitHub("zaen323")
                .create()
        setContentView(aboutPage)
//        setContentView(R.layout.activity_about)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Up button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this) // WOW!~
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
}
