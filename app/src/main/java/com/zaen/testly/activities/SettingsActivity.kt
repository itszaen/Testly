package com.zaen.testly.activities

import android.annotation.SuppressLint
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.support.v4.app.NavUtils
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

import com.zaen.testly.R

import butterknife.ButterKnife
import butterknife.Unbinder
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.activities.auth.Auth
import com.zaen.testly.activities.auth.LoginActivity
import com.zaen.testly.auth.FirebaseTestly
import com.zaen.testly.auth.SignupUserinfo
import com.zaen.testly.fragments.settings.DeveloperSettingsMainFragment
import com.zaen.testly.fragments.settings.SettingsMainFragment
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.form_signup_userinfo.*

class SettingsActivity : AppCompatActivity(),
                SettingsMainFragment.FragmentClickListener,
                DeveloperSettingsMainFragment.FragmentClickListener,
                FirebaseTestly.HandleTask{
    companion object {
        val TAG = "SettingsActivity"
    }
    private lateinit var unbinder: Unbinder
    val transaction = supportFragmentManager
    var toolbar : ActionBar? = null
    var inFragmentLevel = 0
    var firebase: FirebaseTestly? = null

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

    // Firebase in Account Setting
    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            Auth.RC_LOG_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebase = FirebaseTestly(this)
                firebase?.handleGoogleSignInResult(task)
            }
        }
    }
    override fun handleTask(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Log.d(LoginActivity.TAG, "signInWithCredential:success")
            Snacky.builder()
                    .setActivity(this)
                    .setText("Sign in succeeded. You may proceed.")
        } else {
            Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
            Snacky.builder()
                    .setActivity(this)
                    .setText("Sign in Failed.\n Click open to see exception.")
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("OPEN")
                    .setActionClickListener {
                        MaterialDialog.Builder(this)
                                .title("Exception")
                                .content(task.exception.toString())
                                .positiveText(R.string.react_positive)
                    }
                    .error()
                    .show()
        }

    }
}
