package com.zaen.testly

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.zaen.testly.data.UserData
import com.zaen.testly.utils.LogUtils

class TestlyUser(val context: Any){
    companion object {
    }

    interface UserinfoListener{
        fun onUserinfoUpdate(userinfo: UserData?)
    }

    interface ProfileUpdateListener{
        fun onProfileUpdated(successful: Boolean, exception: Exception?)
    }

    var currentUser: FirebaseUser? = null
    var userinfo: UserData? = null

    private var userCollectionRef = FirebaseFirestore.getInstance().collection("users")
    var userinfoSnapshot: DocumentSnapshot? = null

    init {
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    fun isSignedIn():Boolean{
        return currentUser != null
    }

    fun addUserinfoListener(listener: UserinfoListener){
        TestlyFirestore(this).addDocumentListener(userCollectionRef.document(currentUser!!.uid),object: TestlyFirestore.DocumentListener{
            override fun onDocumentUpdate(path: DocumentReference, snapshot: DocumentSnapshot?,exception: Exception?) {
                if (snapshot != null) {
                    userinfoSnapshot = snapshot
                    userinfo = UserData.getUserDataFromDocument(snapshot)
                    listener.onUserinfoUpdate(userinfo)
                } else {
                    userinfoSnapshot = null
                    listener.onUserinfoUpdate(null)
                }
            }
            override fun handleListener(registration: ListenerRegistration?) {
            }
        })
    }

    fun updateProfile(request: UserProfileChangeRequest, listener:TestlyUser.ProfileUpdateListener){
        currentUser!!.updateProfile(request)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d(LogUtils.TAG(context),"User profile updated.")
                        listener.onProfileUpdated(true, null)
                    } else {
                        Log.d(LogUtils.TAG(context),"User profile update failed. Exception: ${it.exception}")
                        listener.onProfileUpdated(false, it.exception)
                    }
                }
                .addOnFailureListener{
                    Log.d(LogUtils.TAG(context),"User profile update failed. Exception: $it")
                    listener.onProfileUpdated(false, it)
                }
    }

}