package com.zaen.testly.activities.cas

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity

class CasViewerActivity: BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_cas_viewer
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        val documentId = intent.extras["documentId"] as String
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

    override fun onStart() {
        super.onStart()
    }


}