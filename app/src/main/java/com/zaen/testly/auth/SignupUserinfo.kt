package com.zaen.testly.auth

import android.app.Activity
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jaredrummler.materialspinner.MaterialSpinner
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.zaen.testly.R
import com.zaen.testly.R.id.*
import com.zaen.testly.activities.auth.Auth
import com.zaen.testly.activities.auth.SignupActivity
import kotlinx.android.synthetic.main.form_signup_userinfo.*
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by zaen on 3/26/18.
 */

open class SignupUserinfo (context: Activity, usernameInput: TextInputLayout, usernameEdit: TextInputEditText, fullnameLastInput: TextInputLayout,
                           fullnameLastEdit: TextInputEditText, fullnameFirstInput: TextInputLayout, fullnameFirstEdit: TextInputEditText,
                           schoolSpinner: SearchableSpinner, gradeSpinner: MaterialSpinner, classSpinner: MaterialSpinner){
    var schoolSnapList = ArrayList<DocumentSnapshot>()
    var schoolIdList = ArrayList<String>()
    var schoolDataList = HashMap<String,Any>()
    var schoolNameList = ArrayList<String>()
    var gradeSnapList = ArrayList<DocumentSnapshot>()
    var gradeDataList = HashMap<String,Any>()
    var gradeNameList = ArrayList<String>()
    var classSnapList = ArrayList<DocumentSnapshot>()
    var classDataList = HashMap<String,Any>()
    var classNameList = ArrayList<String>()
    // Userinfo Input
    val usernameInput = usernameInput
    val usernameEdit = usernameEdit
    val fullnameLastInput = fullnameLastInput
    val fullnameLastEdit = fullnameLastEdit
    val fullnameFirstInput = fullnameFirstInput
    val fullnameFirstEdit = fullnameFirstEdit
    val schoolSpinner = schoolSpinner
    val gradeSpinner = gradeSpinner
    val classSpinner = classSpinner
    // Userinfo Store
    var usernameStr: String? = null
    var firstNameStr: String? = null
    var lastNameStr: String? = null
    var schoolSelected: String? = null // Need, when user set the school to blank, set grade spinner unusable
    var schoolNameStr: String? = null
    var schoolIdStr: String? = null
    var gradeStr: String? = null
    var classStr: String? = null
    // Register Flags
    var isUpdateProfileCompleted = false
    var isFirestoreCompleted = false
    //var isAnalyticsCompleted = false

    val context = context
    var mExceptionHandler: ExceptionHandler? = null
    private var mSuccessListener: SuccessListener? = null

    init{
        schoolSpinner.setTitle("Select School")
        schoolSpinner.setPositiveButton("OK")
        gradeSpinner.isEnabled = false
        classSpinner.isEnabled = false
        if (context is ExceptionHandler) {
            mExceptionHandler = context
        }
        if (context is SuccessListener) {
            mSuccessListener = context
        }
    }
    fun Build():SignupUserinfo{
        val schoolRef = FirebaseFirestore.getInstance().collection("schools")
        schoolRef.get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        Log.d(SignupActivity.TAG,"Getting school names..")
                        schoolSnapList.clear()
                        schoolNameList.clear()
                        for (schoolSnap in it.result){
                            schoolSnapList.add(schoolSnap)
                            schoolIdList.add(schoolSnap.id as String)
                            schoolNameList.add(schoolSnap.get("name") as String)
                        }
                        val schoolSpinnerArrayAdapter = ArrayAdapter<String>(context, R.layout.view_item_spinner_schools_signup,schoolNameList)
                        schoolSpinner.adapter = schoolSpinnerArrayAdapter

                    } else {
                        mExceptionHandler?.onExceptionSnacky(it.exception as kotlin.Exception, "Error getting school names. Exception: ${it.exception}","An error occurred trying to get the list of schools. Click open to see the exception.")
                    }
                }
        // School Selected
        schoolSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                schoolNameStr = schoolNameList[position]
                schoolIdStr = schoolIdList[position]
                schoolSnapList[position].reference.collection("grades").get()
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                Log.d(SignupActivity.TAG,"Getting grade names...")
                                gradeSnapList.clear()
                                gradeNameList.clear()
                                for (gradeSnap in it.result){
                                    gradeSnapList.add(gradeSnap)
                                    gradeNameList.add(gradeSnap.get("name") as String)
                                }
                                val gradeSpinnerArrayAdapter = ArrayAdapter<String>(context,R.layout.view_item_spinner_schools_signup,gradeNameList)
                                gradeSpinner.setAdapter(gradeSpinnerArrayAdapter)
                                gradeSpinner.isEnabled = true
                            } else {
                                mExceptionHandler?.onExceptionSnacky(it.exception as kotlin.Exception, "Error getting school names. Exception: ${it.exception}","An error occurred trying to get the list of grades. Click open to see the exception.")
                            }
                        }
            }

        }
        // Grade selected
        gradeSpinner.setOnItemSelectedListener { view, position, id, item ->
            gradeStr = gradeNameList[position]
            gradeSnapList[position].reference.collection("classes").get()
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Log.d(SignupActivity.TAG,"Getting class names...")
                            classSnapList.clear()
                            classNameList.clear()
                            for (classSnap in it.result){
                                classSnapList.add(classSnap)
                                classNameList.add(classSnap.get("name") as String)
                            }
                            val classSpinnerArrayAdapter = ArrayAdapter<String>(context,R.layout.view_item_spinner_schools_signup,classNameList)
                            classSpinner.setAdapter(classSpinnerArrayAdapter)
                            classSpinner.isEnabled = true
                        } else {
                            mExceptionHandler?.onExceptionSnacky(it.exception as kotlin.Exception, "Error getting school names. Exception: ${it.exception}","An error occurred trying to get the list of classes. Click open to see the exception.")
                        }
                    }
        }
        // Class selected
        classSpinner.setOnItemSelectedListener { view, position, id, item -> classStr = classNameList[position] }
        return this
    }
    fun checkInfoField():Boolean{
        usernameInput.error = null
        fullnameFirstInput.error = null
        fullnameLastInput.error = null
        usernameStr = usernameEdit.text.toString()
        firstNameStr = fullnameFirstEdit.text.toString()
        lastNameStr = fullnameLastEdit.text.toString()

        var cancel = false
        var focusView: View? = null

        // For the first view to receive focusView, hence the order.

        // Check Last name
        if (TextUtils.isEmpty(lastNameStr)){
            fullnameLastInput.error = context.resources.getString(R.string.error_field_required)
            focusView = fullnameLastEdit
            cancel = true
        }
        // Check first name
        if (TextUtils.isEmpty(firstNameStr)){
            fullnameFirstInput.error = context.resources.getString(R.string.error_field_required)
            focusView = fullnameFirstEdit
            cancel = true
        }
        // Check username
        if (TextUtils.isEmpty(usernameStr)){
            usernameInput.error = context.resources.getString(R.string.error_field_required)
            focusView = usernameEdit
            cancel = true
        }
        else if (usernameStr!!.length > usernameInput.counterMaxLength){
            usernameInput.error = context.resources.getString(R.string.error_max_length_exceeded)
            focusView = usernameEdit
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            return false
        } else {
            return true
        }
    }
    // Register userinfo
    fun registerUserInfo(){
        val user = FirebaseAuth.getInstance().currentUser!!
        val db = FirebaseFirestore.getInstance()
        val userinfo = HashMap<String,Any?>()
        // Username
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(usernameStr)
                .build()
        user.updateProfile(profileUpdates)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d(Auth.TAG,"Username Updated.")
                        onCompleteListenerForRegisterInfo(1)
                    }
                }
                .addOnFailureListener{
                    exception -> onRegisterFirebaseFailure(exception,"username")
                }

        // Firestore (Username, fullname, school, grade, class)
        userinfo["uid"] = user.uid
        userinfo["admin"] = false
        userinfo["developer"] = false
        userinfo["provider"] = false
        userinfo["username"] = usernameStr
        userinfo["last"] = lastNameStr
        userinfo["first"] = firstNameStr
        userinfo["schoolName"] = schoolNameStr
        userinfo["school"] = schoolIdStr
        userinfo["grade"] = gradeStr
        userinfo["class"] = classStr
        db.collection("users")
                .document(user.uid)
                .set(userinfo)
                .addOnSuccessListener {
                    Log.d(Auth.TAG, "Userinfo -> Firestore Success.\n DocumentSnapshot added with uid: ${user.uid}")
                    onCompleteListenerForRegisterInfo(2)
                }
                .addOnFailureListener {
                    exception -> onRegisterFirebaseFailure(exception,"userinfo")
                }


        // Analytics for school
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
        mFirebaseAnalytics.setUserProperty(
                "Prop1", schoolIdStr
        )
        mFirebaseAnalytics.setUserProperty(
                "Prop2", gradeStr
        )
    }
    //
    fun onCompleteListenerForRegisterInfo(flag: Int){
        when (flag){
            1 -> { isUpdateProfileCompleted = true }
            2 -> { isFirestoreCompleted = true }
        //3 -> { isAnalyticsCompleted = true }
        }
        if (isUpdateProfileCompleted && isFirestoreCompleted){
            mSuccessListener?.onSuccess()
        }
    }

    fun onRegisterFirebaseFailure(exception: Exception, case: String){
        mExceptionHandler?.onExceptionSnacky(exception,"$case -> Firestore failed. Exception: $exception","An error has occurred storing $case.\n Click open to see exception.")
    }
    interface ExceptionHandler{
        fun onExceptionSnacky(e:kotlin.Exception, logText: String, errorText: String)
    }
    interface SuccessListener{
        fun onSuccess()
    }
}