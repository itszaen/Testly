package com.zaen.testly.fragments.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.stephentuso.welcome.WelcomeHelper
import com.zaen.testly.R
import com.zaen.testly.activities.Intro2Activity
import java.util.*

/**
 * Created by zaen on 3/1/18.
 */
class SettingsMainFragment : Fragment() {
    companion object {
        private val RC_SIGN_IN = 101
    }
    var activity: Activity? = null
    lateinit var toolbar :Toolbar
    private var mListener: FragmentClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentClickListener){
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        return inflater.inflate(R.layout.fragment_settings_main,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
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
            if (resultCode == AppCompatActivity.RESULT_OK){
                // startActivity(SettingsActivity.createIntent(this,response))
                getActivity()?.finish()
            } else {
                //Failed
                if (response == null){ // pressed back, cancelled
                    Toast.makeText(activity,"Sign in cancelled", Toast.LENGTH_SHORT).show(); return}
                if (response?.errorCode == ErrorCodes.NO_NETWORK){ // network error
                    Toast.makeText(activity,"Internet Error", Toast.LENGTH_SHORT).show(); return}
                // unknown
                Toast.makeText(activity,"Unknown Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OnClick(R.id.pref_welcome)
    fun onWelcomeClicked(view:View){
        WelcomeHelper(activity, Intro2Activity::class.java).forceShow()
    }

    @OnClick(R.id.pref_provider)
    fun onProviderSettingClicked(view:View){
        mListener?.onFragmentCalled(ProviderSettingsMainFragment(),"Provider Settings")
    }

    @OnClick(R.id.pref_developer)
    fun onDeveloperSettingClicked(view:View){
        mListener?.onFragmentCalled(DeveloperSettingsMainFragment(),"Developer Settings")
    }

    interface FragmentClickListener{
        fun onFragmentCalled(newFragment:Fragment,title:String)
    }
}