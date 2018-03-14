package com.zaen.testly.activities.auth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.jaredrummler.materialspinner.MaterialSpinner
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.zaen.testly.R
import com.zaen.testly.R.id.spinner_signup_grade
import com.zaen.testly.R.id.spinner_signup_school
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {
    companion object {
        val TAG = "SignupActivity"
        val RC_LOG_IN = 101
        val RC_SIGN_UP = 102
    }
    @BindView(R.id.signup_greeting)
    lateinit var greeting: TextView
    val transaction = supportFragmentManager
    var emailSelected = false
    var googleSelected = false
    var facebookSelected = false
    var twitterSelected = false
    var githubSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        val greetingsList = resources.getStringArray(R.array.signup_greetings)
        greeting.setText(greetingsList[Random().nextInt(greetingsList.size)])

        // School spinner
        spinner_signup_school.setTitle("Select School")
        spinner_signup_school.setPositiveButton("OK")

        // Grade spinner
        spinner_signup_grade.setItems("---")
    }

    fun onEmailSelect(view: View){
        emailSelected = true
        googleSelected = false
        facebookSelected = false
        twitterSelected = false
        githubSelected = false
        emailPasswordVisible(emailSelected)
    }
    fun onGoogleSelect(view: View){
        emailSelected = false
        googleSelected = true
        facebookSelected = false
        twitterSelected = false
        githubSelected = false
        emailPasswordVisible(emailSelected)

    }
    fun onFacebookSelect(view: View){
        emailSelected = false
        googleSelected = false
        facebookSelected = true
        twitterSelected = false
        githubSelected = false
        emailPasswordVisible(emailSelected)

    }
    fun onTwitterSelect(view: View){
        emailSelected = false
        googleSelected = false
        facebookSelected = false
        twitterSelected = true
        githubSelected = false
        emailPasswordVisible(emailSelected)

    }
    fun onGithubSelect(view: View){
        emailSelected = false
        googleSelected = false
        facebookSelected = false
        twitterSelected = false
        githubSelected = true
        emailPasswordVisible(emailSelected)

    }
    fun emailPasswordVisible(yes:Boolean){
        if (yes) {
            signup_emailPassword.setVisibility(View.VISIBLE)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,resources.getDimension(R.dimen.small_spacing).toInt(),0,0)
            signup_form_info.setLayoutParams(params)
        } else {
            signup_emailPassword.setVisibility(View.GONE)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,resources.getDimension(R.dimen.medium_spacing).toInt(),0,0)
            signup_form_info.setLayoutParams(params)
        }
    }
}
