package com.zaen.testly.auth

import android.app.Activity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.wuman.android.auth.OAuthManager
import com.zaen.testly.R
import com.zaen.testly.activities.auth.LoginActivity
import com.zaen.testly.utils.LogUtils
import de.mateware.snacky.Snacky
import java.util.*

/**
 * Created by zaen on 3/25/18.
 */
open class TestlyFirebaseAuth(context: Activity){
    companion object {
    }
    var mAuth: FirebaseAuth? = null

    var mGoogleSignInClient: GoogleSignInClient? = null
    var mFacebookCallbackManager: CallbackManager? = null
    var mTwitterAuthClient: TwitterAuthClient? = null
    var mGithubOAuth: OAuthManager? = null

    var mListener: HandleTask? = null

    var context = context
    var notImplementedBuilder: MaterialDialog.Builder? = null

    init{
        if (context is HandleTask){
            mListener = context
        }
        mAuth = FirebaseAuth.getInstance()

        // Google AuthActivity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        // Facebook AuthActivity
        mFacebookCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        firebaseAuthWithFacebook(result?.getAccessToken())
                    }

                    override fun onCancel() {
                        Snacky.builder()
                                .setActivity(context)
                                .info()
                                .setText("Facebook authentication cancelled.")
                    }

                    override fun onError(error: FacebookException?) {
                        if (error != null) {
                            Snacky.builder()
                                    .setActivity(context)
                                    .error()
                                    .setText("An error has occured.")
                        } else {
                            Snacky.builder()
                                    .setActivity(context)
                                    .error()
                                    .setText("An unknown error has occured.")
                        }
                    }
                })

        // Twitter AuthActivity
        val twitterConfig = TwitterConfig.Builder(context)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig("1hIteTpSElBMDilVOndDPLsJN", "b4phA6d5ey4zmM3lzWi7er4ltyOunV2wuFNYRFuiDB1L7LiuXw"))
                .debug(true)
                .build()
        Twitter.initialize(twitterConfig)
        mTwitterAuthClient = TwitterAuthClient()

        //Github
        notImplementedBuilder = MaterialDialog.Builder(context)
                .title(R.string.not_implemented_title)
                .content(R.string.not_implemented_content)
                .positiveText(R.string.react_positive)
    }

    fun googleAuth(request: Int){
        val intent = mGoogleSignInClient?.signInIntent!!
        startActivityForResult(context,intent, request,null)
    }

    fun facebookAuth(){
        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("email","public_profile"))
    }

    fun twitterAuth(){
        mTwitterAuthClient?.authorize(context,object: Callback<TwitterSession>(){
            override fun success(result: Result<TwitterSession>?) {
                firebaseAuthWithTwitter(result)
            }

            override fun failure(exception: TwitterException?) {
                Snacky.builder()
                        .setActivity(context)
                        .error()
                        .setText("An error has occurred.")
            }
        })
    }

    fun githubAuth(){
        notImplementedBuilder?.build()?.show()
//        val callback = object:OAuthManager.OAuthCallback<Credential>{
//            override fun run(future: OAuthManager.OAuthFuture<Credential>?) {
//                val credential = future?.getResult()
//            }
//        }
//        mGitHubOAuth?.authorizeImplicitly("userID",callback,android.os.Handler())
//        Handler.Callback {  }
//        firebaseAuthWithGitHub(credential.accessToken)
    }

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try{
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {
            Log.w(LoginActivity.TAG, "handleGoogleSignInResult failed. API Exception: ${e.statusCode}")
            MaterialDialog.Builder(context)
                    .title(R.string.error_auth_google_title)
                    .content(R.string.error_auth_google_content)
                    .positiveText(R.string.react_positive)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        Log.d(LoginActivity.TAG, "firebaseAuthWithGoogle:" + account.id)
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(context) { mListener?.handleTask(it) }
                ?.addOnFailureListener { Log.w(LogUtils.TAG(this),"firebaseAuthWithGoogle failed. Exception: $it") }
    }
    private fun firebaseAuthWithFacebook(token: AccessToken?){
        Log.d(LoginActivity.TAG, "firebaseAuthWithFacebook:" + token!!.token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(context) { mListener?.handleTask(it) }
                ?.addOnFailureListener { Log.w(LogUtils.TAG(this),"firebaseAuthWithFacebook failed. Exception: $it") }
    }
    private fun firebaseAuthWithTwitter(session: Result<TwitterSession>?){
        Log.d(LoginActivity.TAG, "firebaseAuthWithTwitter:$session")
        val credential = TwitterAuthProvider.getCredential(
                session!!.data?.authToken?.token!!,
                session.data?.authToken?.secret!!)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(context) { mListener?.handleTask(it) }
                ?.addOnFailureListener { Log.w(LogUtils.TAG(this),"firebaseAuthWithTwitter failed. Exception: $it") }
    }
    private fun firebaseAuthWithGitHub(token: String){
        Log.d(LoginActivity.TAG, "firebaseAuthWithGitHub:$token")
        val credential = GithubAuthProvider.getCredential(token)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(context) { mListener?.handleTask(it) }
                ?.addOnFailureListener { Log.w(LogUtils.TAG(this),"firebaseAuthWithGitHub failed. Exception: $it") }
    }
    interface HandleTask {
        fun handleTask(task: Task<AuthResult>)
    }

}