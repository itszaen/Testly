package com.zaen.testly.activities

import android.annotation.TargetApi
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.text.TextUtils
import android.view.MenuItem
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.text.LoginFilter
import android.view.View

import com.zaen.testly.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.app_bar_main.*


import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.zaen.testly.activities.auth.LoginActivity

class SettingsActivity : AppCompatActivity(){
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        unbinder = ButterKnife.bind(this)

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

    // Button Clicked
    @OnClick (R.id.pref_account)
    fun onAccountSettingClicked(view:View){
        intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
    @OnClick (R.id.pref_provider)
    fun onProviderSettingClicked(view:View){

    }
    @OnClick (R.id.pref_developer)
    fun onDeveloperSettingClicked(view:View){

    }


}
