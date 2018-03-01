package com.zaen.testly.activities

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import com.zaen.testly.R

import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.zaen.testly.fragments.DashboardFragment
import com.zaen.testly.fragments.settings.SettingsMainFragment
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class SettingsActivity : AppCompatActivity(){
    companion object {
        private val TAG = "SettingsActivity"
    }
    private lateinit var unbinder: Unbinder
    val transaction = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        toolbar?.setDisplayShowTitleEnabled(true)

        unbinder = ButterKnife.bind(this)

        if (settings_fragment_container != null) {
            if (savedInstanceState != null) {
                return;
            }
            val mainFragment = SettingsMainFragment()
            mainFragment.setArguments(getIntent().getExtras())
            transaction.beginTransaction()
                    .add(R.id.settings_fragment_container, mainFragment).commit()
        }

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
    override fun onDestroy(){
        unbinder?.unbind()
        super.onDestroy()
    }

}
