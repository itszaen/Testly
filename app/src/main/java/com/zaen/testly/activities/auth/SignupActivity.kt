package com.zaen.testly.activities.auth

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.zaen.testly.R
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : Auth() {
    companion object {
        val TAG = "SignupActivity"
        val AUTH_EMAIL = 1
        val AUTH_GOOGLE = 2
        val AUTH_FACEBOOK = 3
        val AUTH_TWITTER = 4
        val AUTH_GITHUB = 5
    }
    @BindView(R.id.signup_greeting)
    lateinit var greeting: TextView
    var authMethod : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        // Greetings
        val greetingsList = resources.getStringArray(R.array.signup_greetings)
        greeting.setText(greetingsList[Random().nextInt(greetingsList.size)])

        // School spinner
        spinner_signup_school.setTitle("Select School")
        spinner_signup_school.setPositiveButton("OK")

        // Grade spinner
        spinner_signup_grade.setItems("---")
    }


    fun onSocialButtonSelected(view:View){
        signup_emailPassword.setVisibility(View.GONE)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,resources.getDimension(R.dimen.medium_spacing).toInt(),0,0)
        signup_form_info.setLayoutParams(params)
        btn_mail.setBorderWidth(0)
        btn_google.setBorderWidth(0)
        btn_facebook.setBorderWidth(0)
        btn_twitter.setBorderWidth(0)
        btn_github.setBorderWidth(0)
        when(view.id){
            btn_mail.id -> {
                signup_emailPassword.setVisibility(View.VISIBLE)
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(0,resources.getDimension(R.dimen.small_spacing).toInt(),0,0)
                signup_form_info.setLayoutParams(params)
                btn_mail.setBorderWidth(5)
                authMethod = AUTH_EMAIL
            }
            btn_google.id -> {
                btn_google.setBorderWidth(5)
                authMethod = AUTH_GOOGLE
            }
            btn_facebook.id -> {
                btn_facebook.setBorderWidth(5)
                authMethod = AUTH_FACEBOOK

            }
            btn_twitter.id -> {
                btn_twitter.setBorderWidth(5)
                authMethod = AUTH_TWITTER

            }
            btn_github.id -> {
                btn_github.setBorderWidth(5)
                authMethod = AUTH_GITHUB
            }
        }

    }
    fun onSignUp(view:View){
        when(authMethod){
            null -> {}
            AUTH_EMAIL -> emailAuth(input_email,input_password)
            AUTH_GOOGLE -> googleAuth()
            AUTH_FACEBOOK -> facebookAuth()
            AUTH_TWITTER -> twitterAuth()
            AUTH_GITHUB -> githubAuth()
        }
    }
    override fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    override fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}
