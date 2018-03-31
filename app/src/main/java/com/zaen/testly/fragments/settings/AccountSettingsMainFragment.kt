package com.zaen.testly.fragments.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import butterknife.ButterKnife
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.R
import kotlinx.android.synthetic.main.fragment_settings_account_main.*
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat.startActivity
import butterknife.OnClick
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.activities.auth.Auth
import com.zaen.testly.auth.FirebaseTestly


/**
 * Created by zaen on 3/25/18.
 */
class AccountSettingsMainFragment : Fragment(){
    companion object {
        val TAG = "AccountSettingsMainFrag"
    }

    var activity: Activity? = null
    var firebase: FirebaseTestly? = null
    lateinit var toolbar : Toolbar
    private var mListener: DeveloperSettingsMainFragment.FragmentClickListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DeveloperSettingsMainFragment.FragmentClickListener){
            mListener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        return inflater.inflate(R.layout.fragment_settings_account_main,container,false)
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

    interface FragmentClickListener{
        fun onFragmentCalled(newFragment:Fragment,title:String)
    }

    // XML
    @OnClick(R.id.pref_account_update_email)
    fun onUpdateEmail(view:View){
        MaterialDialog.Builder(activity!!)
                .title("Change email address")
                .positiveText(R.string.react_change)
                .negativeText(R.string.react_refuse)
                .onNegative { dialog, which -> dialog.dismiss()}
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("abc_xyz@gmail.com","",false,
                        { dialog, input -> FirebaseAuth.getInstance().currentUser!!.updateEmail(input.toString())
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        Log.d(TAG,"User email updated: $input")
                                    }
                                }
                        })
                .show()
    }

    @OnClick(R.id.pref_account_update_password)
    fun onUpdatePassword(view:View){
        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email!!
        MaterialDialog.Builder(activity!!)
                .title("Reset password")
                .content("An email with instructions to reset your password will be sent to $email.")
                .positiveText(R.string.react_positive)
                .negativeText(R.string.react_refuse)
                .onPositive{dialog,which->
                     auth.sendPasswordResetEmail(email)
                             .addOnCompleteListener{
                                 if(it.isSuccessful){
                                  Log.d(TAG, "Password Reset Email sent to $email")
                                 }
                             }
                }
                .onNegative{dialog,which->dialog.dismiss()}
                .show()
    }

    @OnClick(R.id.pref_account_logout)
    fun onLogout(view:View){
        MaterialDialog.Builder(activity!!)
                .title("Log Out")
                .content("You will be logged out from Testly. \n\n※You will not be able to use Testly until you sign in again.")
                .positiveText(R.string.react_logout)
                .negativeText(R.string.react_refuse)
                .onPositive{dialog,which->
                    FirebaseAuth.getInstance().signOut()
                    val intent =  activity?.baseContext?.packageManager?.getLaunchIntentForPackage(
                            activity?.baseContext?.packageName
                    )
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    activity?.finish()
                    startActivity(intent)
                }
                .onNegative{dialog,which->dialog.dismiss()}
                .show()
    }

    @OnClick(R.id.pref_account_delete)
    fun onDelete(view:View){
        MaterialDialog.Builder(activity!!)
                .title("Delete Account")
                .content("Warning: This action cannot be undone!")
                .positiveText(R.string.react_delete)
                .negativeText(R.string.react_refuse)
                .onPositive{dialog,which->
                    try {
                        val user = FirebaseAuth.getInstance().currentUser!!
                        user.delete()
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        Log.d(TAG,"User account deleted.")
                                        FirebaseFirestore.getInstance().collection("users").document(user.uid)
                                                .delete()
                                                .addOnSuccessListener { Log.d(TAG,"Userinfo deleted.") }
                                                .addOnFailureListener { Log.w(TAG,"Userinfo deletion failed. Exception: $it")}
                                        val intent =  activity?.baseContext?.packageManager?.getLaunchIntentForPackage(
                                                activity?.baseContext?.packageName
                                        )
                                        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        activity?.finish()
                                        startActivity(intent)
                                    }else{
                                        dialog.dismiss()
                                        onReauthenticateUser()
                                    }
                                }
                    } catch (e: FirebaseAuthRecentLoginRequiredException){
                        dialog.dismiss()
                        onReauthenticateUser()
                    }
                }
                .onNegative{dialog,which->dialog.dismiss()}
                .show()
    }

    fun onReauthenticateUser(){
        MaterialDialog.Builder(activity!!)
                .title("Re-authenticate")
                .content("The action requires you to be signed in recently.\nTo continue, please click OK to re-authenticate.")
                .positiveText(R.string.react_positive)
                .negativeText(R.string.react_refuse)
                .onPositive{dialog,which->
                    firebase = FirebaseTestly(activity!!)
                        when (FirebaseAuth.getInstance().currentUser!!.providerData.get(1).providerId){
                        "password" -> {}
                        "google.com" -> {FirebaseTestly(activity!!).googleAuth(Auth.RC_LOG_IN)}
                        "facebook.com" -> {}
                        "twitter.com" -> {}
                        "github.com" -> {}

                    }
                }
                .onNegative{dialog,which->dialog.dismiss()}
                .show()
    }


}