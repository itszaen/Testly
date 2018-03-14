package com.zaen.testly.activities.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.SyncStateContract.Helpers.update
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.AndroidException
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.zaen.testly.R
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.auth.oauth2.ClientParametersAuthentication
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.http.GenericUrl
import com.google.api.client.json.jackson.JacksonFactory
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.wuman.android.auth.AuthorizationFlow
import com.wuman.android.auth.AuthorizationUIController
import com.wuman.android.auth.DialogFragmentController
import com.wuman.android.auth.OAuthManager
import com.wuman.android.auth.oauth2.store.SharedPreferencesCredentialStore
import com.zaen.testly.App.Companion.getInstance
import com.zaen.testly.activities.MainActivity
import com.zaen.testly.activities.auth.LoginActivity.Companion.RC_LOG_IN
import de.mateware.snacky.Snacky
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    companion object {
        val TAG = "LoginActivity"
        val RC_LOG_IN = 101
        val RC_SIGN_UP = 102
    }

    @BindView(R.id.login_greeting)
    lateinit var greeting: TextView
    val transaction = supportFragmentManager
    var notImplementedBuilder :MaterialDialog.Builder? = null
    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient : GoogleSignInClient? = null
    var mCallbackManager: CallbackManager? = null
    var mTwitterAuthClient: TwitterAuthClient? = null
    var mGithubOAuth: OAuthManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        notImplementedBuilder = MaterialDialog.Builder(this)
                .title(R.string.not_implemented_title)
                .content(R.string.not_implemented_content)
                .positiveText(R.string.react_positive)

        val greetingsList = resources.getStringArray(R.array.login_greetings)
        greeting.setText(greetingsList[Random().nextInt(greetingsList.size)])

        // Firebase
        mAuth = FirebaseAuth.getInstance()
        // Google Auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // Facebook Auth
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager,object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthWithFacebook(result?.getAccessToken())
            }
            override fun onCancel() {
                Snacky.builder()
                        .setActivity(this@LoginActivity)
                        .info()
                        .setText("Facebook authentication cancelled.")
            }
            override fun onError(error: FacebookException?){
                if (error != null){
                    Snacky.builder()
                            .setActivity(this@LoginActivity)
                            .error()
                            .setText("An error has occured.")
                } else {
                    Snacky.builder()
                            .setActivity(this@LoginActivity)
                            .error()
                            .setText("An unknown error has occured.")
                }
            }
        })

        // Twitter Auth
        val twitterConfig = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig("1hIteTpSElBMDilVOndDPLsJN","b4phA6d5ey4zmM3lzWi7er4ltyOunV2wuFNYRFuiDB1L7LiuXw"))
                .debug(true)
                .build()
        Twitter.initialize(twitterConfig)
        mTwitterAuthClient = TwitterAuthClient()

        // Github Auth

        val credentialStore = SharedPreferencesCredentialStore(this,getString(R.string.pref_github_token),JacksonFactory())
        val authFlowBuilder = AuthorizationFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory(),
                GenericUrl("https://github.com/login/oauth/access_token"),
                ClientParametersAuthentication("ce82854fc4ca8465da94", "96fcb57eead6633464e4ef6216f0e1fca0e9477c"),
                "ce82854fc4ca8465da94","https://github.com/login/oauth/authorize")
        authFlowBuilder.setCredentialStore(credentialStore)
//        authFlowBuilder.setScopes()
        val flow = authFlowBuilder.build()

        val uiController = object:DialogFragmentController(fragmentManager,true){
            @Throws(IOException::class)
            override fun getRedirectUri():String {
                return "https://github.com/login/oauth/access_token"
            }
            override fun isJavascriptEnabledForWebView():Boolean {
                return true
            }
            override fun disableWebViewCache():Boolean {
                return false
            }
            override fun removePreviousCookie():Boolean {
                return false
            }
        }
        mGithubOAuth = OAuthManager(flow,uiController)
    }

    override fun onStart() {
        super.onStart()
        val accountGoogle = GoogleSignIn.getLastSignedInAccount(this)
        val accountFirebase = mAuth?.currentUser
        if ((accountGoogle != null)or(accountFirebase != null)){
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    fun onForgotPassword(view: View){
        setResult(RESULT_OK,intent)
        finish()
    }
    fun onCreateAccount(view:View){
        setResult(RC_SIGN_UP,intent)
        finish()
    }
    fun onGoogleAuth(view:View){
        startActivityForResult(mGoogleSignInClient?.signInIntent,RC_LOG_IN)
    }
    fun onFacebookAuth(view:View){
        LoginManager.getInstance().logInWithReadPermissions(this@LoginActivity,Arrays.asList("email","public_profile"))
    }
    fun onTwitterAuth(view:View){
        mTwitterAuthClient?.authorize(this,object:Callback<TwitterSession>(){
            override fun success(result: Result<TwitterSession>?) {
                firebaseAuthWithTwitter(result)
            }

            override fun failure(exception: TwitterException?) {
                Snacky.builder()
                        .setActivity(this@LoginActivity)
                        .error()
                        .setText("An error has occured.")
            }
        })
    }
    fun onGithubAuth (view:View){
        notImplementedBuilder?.build()?.show()
//        val callback = object:OAuthManager.OAuthCallback<Credential>{
//            override fun run(future: OAuthManager.OAuthFuture<Credential>?) {
//                val credential = future?.getResult()
//            }
//        }
//        mGithubOAuth?.authorizeImplicitly("userID",callback,android.os.Handler())
//        Handler.Callback {  }
//        firebaseAuthWithGithub(credential.accessToken)
    }
    fun onEmailAuth (view:View){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
        mTwitterAuthClient?.onActivityResult(requestCode,resultCode,data)
        when (requestCode){
            RC_LOG_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }
    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try{
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)

        } catch (e:ApiException) {
            Log.w(TAG, "handleSignInResult:failure code="+e.getStatusCode())
            MaterialDialog.Builder(this)
                    .title(R.string.error_auth_google_title)
                    .content(R.string.error_auth_google_content)
                    .positiveText(R.string.react_positive)
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId())
        val credential = GoogleAuthProvider.getCredential(account.getIdToken(),null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, object:OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        handleTask(task)
                    }

                })
    }
    fun firebaseAuthWithFacebook(token: AccessToken?){
        Log.d(TAG, "firebaseAuthWithFacebook:" + token!!.getToken())
        val credential = FacebookAuthProvider.getCredential(token!!.getToken())
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this,object:OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        handleTask(task)
                    }

                })
    }
    fun firebaseAuthWithTwitter(session: Result<TwitterSession>?){
        Log.d(TAG, "firebaseAuthWithTwitter:" + session)
        val credential = TwitterAuthProvider.getCredential(
                session?.data?.authToken?.token!!,
                session?.data?.authToken?.secret!!)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, object:OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        handleTask(task)
                    }
                })
    }
    fun firebaseAuthWithGithub(token: String){
        Log.d(TAG, "firebaseAuthWithGithub:" + token)
        val credential = GithubAuthProvider.getCredential(token)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, object:OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        handleTask(task)
                    }
                })
    }
    fun handleTask(task: Task<AuthResult>){
        if (task.isSuccessful()) {
            Log.d(TAG, "signInWithCredential:success")
            // OK to Use mAuth.getCurrentUser()
            setResult(RESULT_OK,intent)
            finish()
            Toasty.success(this,"Signed in as "+mAuth?.currentUser?.email, Toast.LENGTH_SHORT,true).show()
        } else {
            Log.w(TAG, "signInWithCredential:failure", task.exception)
            Toasty.error(this,"Sign in Failed. See the following error:"+task.exception,Toast.LENGTH_LONG,true).show()
        }

    }
}
