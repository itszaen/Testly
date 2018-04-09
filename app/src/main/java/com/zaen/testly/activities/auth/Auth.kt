package com.zaen.testly.activities.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.R
import com.zaen.testly.auth.FirebaseTestly
import com.zaen.testly.auth.SignupUserinfo
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.form_signup_userinfo.*
import java.util.*

/**
 * Created by zaen on 3/13/18.
 */
abstract class Auth: AppCompatActivity(),
        FirebaseTestly.HandleTask,SignupUserinfo.SuccessListener {
    companion object {
        const val TAG = "Auth"
        const val RC_LOG_IN = 101
        const val RC_SIGN_UP = 102
        const val RC_SIGN_UP_INFO = 103
    }
    var firebase: FirebaseTestly? = null
    var mFirebaseAnalytics: FirebaseAnalytics? = null

    // Login? Signup?
    var request: Int? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase
        firebase = FirebaseTestly(this)
    }

    // Check if already signed in
    override fun onStart(){
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser !=null){
            onSuccess()
        }
    }

    fun googleAuth(){
        firebase?.googleAuth(RC_LOG_IN)
    }
    fun facebookAuth(){
        firebase?.facebookAuth()
    }
    fun twitterAuth(){
        firebase?.twitterAuth()
    }
    fun githubAuth(){
        firebase?.githubAuth()
    }

    fun emailAuth(input_email: TextInputLayout, input_password:TextInputLayout){
        input_email.error = null
        input_password.error = null
        val edit_email = (input_email.getChildAt(0) as FrameLayout).getChildAt(0) as TextInputEditText
        val edit_password = (input_password.getChildAt(0) as FrameLayout).getChildAt(0) as TextInputEditText
        val emailStr = edit_email.text.toString()
        val passwordStr = edit_password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check if password is empty
        if (TextUtils.isEmpty(passwordStr)){
            input_password.error = resources.getString(R.string.error_field_required)
            focusView = edit_password
            cancel = true
        }
        // Check if password is not empty but invalid
        else if (!isPasswordValid(passwordStr)) {
            input_password.error = resources.getString(R.string.error_invalid_password)
            focusView = edit_password
            cancel = true
        }

        // Check if email address is empty
        if (TextUtils.isEmpty(emailStr)) {
            input_email.error = resources.getString(R.string.error_field_required)
            focusView = edit_email
            cancel = true
        }
        // Check if email address is not empty but invalid
        else if (!isEmailValid(emailStr)) {
            input_email.error = resources.getString(R.string.error_invalid_email)
            focusView = edit_email
            cancel = true
        }

        if (cancel){
            focusView?.requestFocus()
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                        override fun onComplete(task: Task<AuthResult>) {
                            handleTask(task)
                        }

                    })
        }
    }
    open fun isEmailValid(email: String): Boolean{
        return true
    }

    open fun isPasswordValid(password: String): Boolean{
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebase?.mFacebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
        firebase?.mTwitterAuthClient?.onActivityResult(requestCode,resultCode,data)
        when (requestCode){
            Auth.RC_LOG_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebase?.handleGoogleSignInResult(task)
            }
        }
    }
    override fun handleTask(task: Task<AuthResult>){
        val mAuth = FirebaseAuth.getInstance()
        if (task.isSuccessful) {
            Log.d(LoginActivity.TAG, "signInWithCredential:success")
            Toasty.success(this,"Signed in as "+mAuth?.currentUser?.email, Toast.LENGTH_SHORT,true).show()
            if (request == Auth.RC_LOG_IN){
                checkUserinfo()
            } else if (request == Auth.RC_SIGN_UP){
                 val userinfo = SignupUserinfo(this,input_username,edit_username,input_fullname_last,edit_fullname_last,input_fullname_first,edit_fullname_first,spinner_signup_school,spinner_signup_grade,spinner_signup_class)
                        .Build()
                userinfo.registerUserInfo()
            }
        } else {
            Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
            Snacky.builder()
                    .setActivity(this)
                    .setText("Sign in Failed.\n Click open to see exception.")
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("OPEN")
                    .setActionClickListener {
                        MaterialDialog.Builder(this@Auth)
                                .title("Exception")
                                .content(task.exception.toString())
                                .positiveText(R.string.react_positive)
                    }
                    .error()
                    .show()
        }

    }
    override fun onSuccess(){
        setResult(RESULT_OK,intent)
        finish()
    }

    fun onExceptionSnacky(exception: Exception,logText:String,errorText:String){
        Log.w(TAG,logText)
        Snacky.builder()
                .setActivity(this)
                .setText(errorText)
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("OPEN")
                .setActionClickListener {
                    MaterialDialog.Builder(this@Auth)
                            .title("Exception")
                            .content(exception.toString())
                            .positiveText(R.string.react_positive)
                            .show()
                }
                .error()
                .show()
    }

    private fun checkUserinfo() {
        val ref = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.get().addOnCompleteListener(object: OnCompleteListener<DocumentSnapshot>{
            override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful){
                    val snapshot = task.result
                    if (snapshot.exists()) {
                        Log.w(TAG,"Snapshot: $snapshot")
                        onSuccess()
                    } else {
                        setResult(RC_SIGN_UP_INFO,intent)
                        finish()
                    }
                } else {
                    onExceptionSnacky(task.exception as kotlin.Exception,
                            "Get userinfo failed. Exception: " + task.exception,
                            "An error has occurred getting your user information. Click open to see exception")
                }
            }
        })

    }
}