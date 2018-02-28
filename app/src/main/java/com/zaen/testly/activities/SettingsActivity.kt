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
import java.util.*

class SettingsActivity : AppCompatActivity(){
    companion object {
        private val RC_SIGN_IN = 101
    }
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        toolbar?.setDisplayShowTitleEnabled(true)

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
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
//                                AuthUI.IdpConfig.EmailBuilder().build(),
//                                AuthUI.IdpConfig.PhoneBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build()
//                                AuthUI.IdpConfig.FacebookBuilder().build(),
//                                AuthUI.IdpConfig.TwitterBuilder().build()
                                ))
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)
            // Successful
            if (resultCode == RESULT_OK){
               // startActivity(SettingsActivity.createIntent(this,response))
                finish()
            } else {
                //Failed
                if (response == null){ // pressed back, cancelled
                    Toast.makeText(this,"Sign in cancelled",Toast.LENGTH_SHORT).show(); return}
                if (response?.errorCode == ErrorCodes.NO_NETWORK){ // network error
                    Toast.makeText(this,"Internet Error",Toast.LENGTH_SHORT).show(); return}
                // unknown
                Toast.makeText(this,"Unknown Error",Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OnClick (R.id.pref_provider)
    fun onProviderSettingClicked(view:View){

    }
    @OnClick (R.id.pref_developer)
    fun onDeveloperSettingClicked(view:View){

    }


}
