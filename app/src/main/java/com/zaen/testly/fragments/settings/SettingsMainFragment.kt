package com.zaen.testly.fragments.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.google.firebase.auth.FirebaseAuth
import com.stephentuso.welcome.WelcomeHelper
import com.zaen.testly.R
import com.zaen.testly.R.string.pref_account_caption
import com.zaen.testly.activities.Intro2Activity
import kotlinx.android.synthetic.main.fragment_settings_main.*

/**
 * Created by zaen on 3/1/18.
 */
class SettingsMainFragment : Fragment() {
    companion object {

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
        pref_account.setCaptionText(resources.getString(R.string.pref_account_caption, FirebaseAuth.getInstance().currentUser!!.email))
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    // Button Clicked
    @Optional
    @OnClick (R.id.pref_account)
    fun onAccountSettingClicked(view:View){
        mListener?.onFragmentCalled(AccountSettingsMainFragment(),"Account Settings")
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