package com.zaen.testly.fragments.settings

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
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.R
import butterknife.OnClick
import butterknife.Unbinder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.activities.SettingsActivity
import com.zaen.testly.activities.auth.Auth
import com.zaen.testly.auth.FirebaseTestly
import de.mateware.snacky.Snacky
import com.zaen.testly.activities.SettingsActivity.Companion.isReauthenticatedDeleteUser


/**
 * Created by zaen on 3/25/18.
 */
class AccountSettingsMainFragment : Fragment(){
    companion object {
        const val TAG = "AccountSettingsMainFrag"
    }

    var activity: Activity? = null
    var firebase: FirebaseTestly? = null
    lateinit var toolbar : Toolbar
    private var mListener: DeveloperSettingsMainFragment.FragmentClickListener? = null
    var unbinder: Unbinder? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DeveloperSettingsMainFragment.FragmentClickListener){
            mListener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        val view = inflater.inflate(R.layout.fragment_settings_account_main,container,false)
        unbinder = ButterKnife.bind(this,view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
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
                    val user = FirebaseAuth.getInstance().currentUser!!
                    if (!isReauthenticatedDeleteUser){
                        dialog.dismiss()
                        reAuthenticateUser()
                    } else {
                        // Delete User info
                        deleteUserInfo(user)
                        // Delete user
                        deleteUser(user,dialog)
                    }
                }
                .onNegative{dialog,which->dialog.dismiss()}
                .show()
    }
    fun onExceptionSnacky(e: Exception){
        Snacky.builder()
                .setActivity(activity)
                .setText("An error has occurred.\nClick open to see exception.")
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("OPEN")
                .setActionClickListener {
                    MaterialDialog.Builder(activity!!)
                            .title("Exception")
                            .content(e.toString())
                            .positiveText(R.string.react_positive)
                            .show()
                }
                .setDuration(Snacky.LENGTH_LONG)
                .error()
                .show()
    }
    private fun reAuthenticateUser(){
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
    fun deleteUser(user: FirebaseUser, dialog: MaterialDialog){
        user.delete()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        Log.d(TAG,"User account deleted.")
                        val intent =  activity?.baseContext?.packageManager?.getLaunchIntentForPackage(
                                activity?.baseContext?.packageName
                        )
                        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        activity?.finish()
                        startActivity(intent)
                    }else{
                        val e = it.exception
                        dialog.dismiss()
                        onExceptionSnacky(e as Exception)
                    }
                }
    }
    fun deleteUserInfo(user: FirebaseUser){
        FirebaseFirestore.getInstance().collection("users").document(user.uid)
                .delete()
                .addOnSuccessListener { Log.d(TAG,"Userinfo deleted.") }
                .addOnFailureListener { Log.w(TAG,"Userinfo deletion failed. Exception: $it")}
    }


}