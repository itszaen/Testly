package com.zaen.testly.fragments.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import butterknife.OnClick
import butterknife.Optional
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.stephentuso.welcome.WelcomeHelper
import com.zaen.testly.R
import com.zaen.testly.activities.Intro2Activity
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.utils.LogUtils.Companion.TAG
import kotlinx.android.synthetic.main.fragment_settings_main.*

/**
 * Created by zaen on 3/1/18.
 */
class SettingsMainFragment : BaseFragment() {
    companion object {
    }

    interface FragmentClickListener{
        fun onFragmentCalled(newFragment:BaseFragment,title:String)
    }

    lateinit var toolbar :Toolbar
    private var mListener: FragmentClickListener? = null
    private var userinfoSnapshot: DocumentSnapshot? = null
    private var hasProviderView: Boolean? = null
    private var hasDeveloperView: Boolean? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FragmentClickListener){
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_settings_main
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pref_account.setCaptionText(resources.getString(R.string.pref_account_caption, FirebaseAuth.getInstance().currentUser!!.email))

        // Hide child
        val userinfoRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        userinfoRef.addSnapshotListener{snapshot,exception ->
            if (exception != null){
                Log.w(TAG(this), "Userinfo listen failed. Exception: $exception")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()){
                Log.d(TAG(this), "Userinfo listened. Current data: ${snapshot.data}")
                onUserinfoUpdate(snapshot)
            } else {
                Log.d(TAG(this), "Userinfo listened. Current data: null")
                onUserinfoUpdate(null)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    // Button Clicked
    @OnClick (R.id.pref_account)
    fun onAccountSettingClicked(view:View){
        mListener?.onFragmentCalled(AccountSettingsMainFragment(),"Account Settings")
    }

    @OnClick(R.id.pref_welcome)
    fun onWelcomeClicked(view:View){
        WelcomeHelper(activity, Intro2Activity::class.java).forceShow()
    }

    @Optional
    @OnClick(R.id.pref_provider)
    fun onProviderSettingClicked(view:View){
        mListener?.onFragmentCalled(ProviderSettingsMainFragment(),"Provider Settings")
    }

    @Optional
    @OnClick(R.id.pref_developer)
    fun onDeveloperSettingClicked(view:View){
        mListener?.onFragmentCalled(DeveloperSettingsMainFragment(),"Developer Settings")
    }

    private fun onUserinfoUpdate(snapshot: DocumentSnapshot?){
        userinfoSnapshot = snapshot
        toggleItemVisibility()
    }
    private fun toggleItemVisibility(){
        if (userinfoSnapshot != null) {
            if (userinfoSnapshot!!.data!!["provider"] as Boolean){
                pref_provider.visibility = View.VISIBLE
            } else {
                pref_provider.visibility = View.GONE
            }
            if (userinfoSnapshot!!.data!!["developer"] as Boolean) {
                pref_developer.visibility = View.VISIBLE
            } else {
                pref_developer.visibility = View.GONE
            }
        }else{
            pref_provider.visibility = View.GONE
            pref_developer.visibility = View.GONE
        }
    }

}