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
/*
class SettingsActivity : AppCompatPreferenceActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ? ... = if available
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this)
            }
            return true
        }
        return super.onMenuItemSelected(featureId, item)
    }

    override fun onIsMultiPane(): Boolean { // if is a tablet
        return isXLargeTablet(this)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // header ... after honeycomb Android 3.0
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) { // header
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || GeneralPreferenceFragment::class.java.name == fragmentName
                || DataSyncPreferenceFragment::class.java.name == fragmentName
                || NotificationPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"))
            bindPreferenceSummaryToValue(findPreference("example_list"))

        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class NotificationPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_notification)
            setHasOptionsMenu(true)

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class DataSyncPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_data_sync)
            setHasOptionsMenu(true)

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val listPreference = preference
                val index = listPreference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            listPreference.entries[index]
                        else
                            null)

            } else if (preference is RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent)

                } else {
                    val ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue))

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null)
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        val name = ringtone.getTitle(preference.getContext())
                        preference.setSummary(name)
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
            true
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }
    }
}
*/