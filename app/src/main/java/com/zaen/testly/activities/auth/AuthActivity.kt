package com.zaen.testly.activities.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.auth.SignupUserinfo
import com.zaen.testly.auth.TestlyFirebaseAuth
import com.zaen.testly.utils.LogUtils
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty

/**
 * Created by zaen on 3/13/18.
 */
abstract class AuthActivity: BaseActivity(),
        TestlyFirebaseAuth.HandleTask,SignupUserinfo.SuccessListener {
    companion object {
        const val RC_LOG_IN = 101
        const val RC_SIGN_UP = 102
        const val RC_SIGN_UP_INFO = 103
    }
    var firebaseAuth: TestlyFirebaseAuth? = null
    var mFirebaseAnalytics: FirebaseAnalytics? = null
    var userinfo: SignupUserinfo? = null

    // Login? Signup?
    var request: Int? = null

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase
        firebaseAuth = TestlyFirebaseAuth(this)
    }

    // Check if already signed in
    override fun onStart(){
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser !=null){
            onSuccess()
        }
    }

    fun googleAuth(){
        firebaseAuth?.googleAuth(RC_LOG_IN)
    }
    fun facebookAuth(){
        firebaseAuth?.facebookAuth()
    }
    fun twitterAuth(){
        firebaseAuth?.twitterAuth()
    }
    fun githubAuth(){
        firebaseAuth?.githubAuth()
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
                    .addOnCompleteListener(this) { handleTask(it) }
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
        firebaseAuth?.mFacebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
        firebaseAuth?.mTwitterAuthClient?.onActivityResult(requestCode,resultCode,data)
        when (requestCode){
            AuthActivity.RC_LOG_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                firebaseAuth?.handleGoogleSignInResult(task)
            }
        }
    }
    override fun handleTask(task: Task<AuthResult>){
        val mAuth = FirebaseAuth.getInstance()
        if (task.isSuccessful) {
            LogUtils.success(this,4,"signInWithCredential")
            Toasty.success(this,"Signed in as "+mAuth?.currentUser?.email, Toast.LENGTH_SHORT,true).show()
            if (request == AuthActivity.RC_LOG_IN){
                checkUserinfo()
            } else if (request == AuthActivity.RC_SIGN_UP){
                userinfo?.registerUserInfo()
            }
        } else {
            LogUtils.failure(this,5, "signInWithCredential(). Exception: ${task.exception}")
            Snacky.builder()
                    .setActivity(this)
                    .setText("Sign in Failed.\n Click open to see exception.")
                    .setDuration(Snacky.LENGTH_LONG)
                    .setActionText("OPEN")
                    .setActionClickListener {
                        MaterialDialog.Builder(this@AuthActivity)
                                .title("Exception")
                                .content(task.exception.toString())
                                .positiveText(R.string.react_positive)
                    }
                    .error()
                    .show()
        }

    }
    override fun onSuccess(){
        setResult(Activity.RESULT_OK,intent)
        onBackPressed()
    }

    private fun checkUserinfo() {
        val ref = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.get().addOnCompleteListener {
            if (it.isSuccessful){
                val snapshot = it.result
                if (snapshot.exists()) {
                    LogUtils.success(this,4,"Userinfo uploaded. Snapshot: $snapshot")
                    onSuccess()
                } else {
                    setResult(RC_SIGN_UP_INFO,intent)
                    finish()
                }
            } else {
                LogUtils.failure(this, 5, "Getting userinfo.", it.exception as Exception)
                snackyException("An error has occurred getting your user information.",it.exception as Exception)
            }
        }

    }
}