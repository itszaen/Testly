package com.zaen.testly.auth.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.zaen.testly.R
import kotlinx.android.synthetic.main.activity_login.*
import kotterknife.bindView
import java.util.*


class LoginActivity : AuthActivity() {
    private val greeting: TextView by bindView(R.id.login_greeting)


    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_login
        super.onCreate(savedInstanceState)
        request = RC_LOG_IN

        val greetingsList = resources.getStringArray(R.array.login_greetings)
        greeting.text = greetingsList[Random().nextInt(greetingsList.size)]

    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        val accountGoogle = GoogleSignIn.getLastSignedInAccount(this)
        val accountFirebase = firebaseAuth?.mAuth?.currentUser
        if ((accountGoogle != null)and(accountFirebase != null)){
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    fun onForgotPassword(view: View){
        // Place holder & backdoor
        onSuccess()
    }
    fun onCreateAccount(view:View){
        setResult(RC_SIGN_UP,intent)
        finish()
    }
    fun onGoogleAuth(view:View){
        googleAuth()
    }
    fun onFacebookAuth(view:View){
        facebookAuth()
    }
    fun onTwitterAuth(view:View){
        twitterAuth()
    }
    fun onGithubAuth (view:View){
        githubAuth()
    }
    fun onEmailAuth (view:View){
        emailAuth(input_email,input_password)
    }

    override fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    override fun isPasswordValid(password: String): Boolean {
        return true
    }



}
