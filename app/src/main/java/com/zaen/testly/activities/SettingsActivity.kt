package com.zaen.testly.activities

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.support.v4.app.NavUtils
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity

import com.zaen.testly.R

import butterknife.ButterKnife
import butterknife.Unbinder
import com.zaen.testly.fragments.settings.DeveloperSettingsMainFragment
import com.zaen.testly.fragments.settings.SettingsMainFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(),
                SettingsMainFragment.FragmentClickListener,
                DeveloperSettingsMainFragment.FragmentClickListener{
    companion object {
        val TAG = "SettingsActivity"
    }
    private lateinit var unbinder: Unbinder
    val transaction = supportFragmentManager
    var toolbar : ActionBar? = null
    var inFragmentLevel = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbar = this.supportActionBar
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
                if (inFragmentLevel > 0) {
                    onBackPressed()
                    inFragmentLevel -= 1
                    if (inFragmentLevel == 0){inMainFragment()}
                    return true
                } else {
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
            }

        // ...
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy(){
        unbinder?.unbind()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (inFragmentLevel > 0) {inFragmentLevel-=1}
        if (inFragmentLevel == 0) {inMainFragment()}
        super.onBackPressed()
    }

    override fun onFragmentCalled(newFragment: Fragment, title: String) {
        val args = Bundle()
        args.putString("TITLE",title)
        newFragment.setArguments(args)
        transaction.beginTransaction()
                .replace(R.id.settings_fragment_container,newFragment)
                .addToBackStack(null)
                .commit()
        toolbar?.setTitle(title)
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        inFragmentLevel += 1
    }
    fun inMainFragment(){
        toolbar?.setTitle(getString(R.string.title_activity_settings))
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
    }

}
