package com.zaen.testly

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.zaen.testly.activities.auth.Auth
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
                    userinfo = getUserinfoFromDocument(snapshot)
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

    fun getUserinfoFromDocument(snapshot: DocumentSnapshot) : UserData{
        return UserData(
                snapshot.get("id") as String,
                snapshot.get("timestamp") as Long,
                snapshot.get("email") as String,
                snapshot.get("profileUrl") as String,
//                            snapshot.get("isAdmin") as Boolean,
//                            snapshot.get("isDeveloper") as Boolean,
//                            snapshot.get("isProvider") as Boolean,
                snapshot.get("admin") as Boolean,
                snapshot.get("developer") as Boolean,
                snapshot.get("provider") as Boolean,
                snapshot.get("displayName") as String,
                snapshot.get("firstName") as String,
                snapshot.get("lastName") as String,
                snapshot.get("schoolId") as String,
                snapshot.get("schoolName") as String,
                snapshot.get("gradeId") as String,
                snapshot.get("grade") as String,
                snapshot.get("classId") as String,
                snapshot.get("class_") as String
        )
    }

    fun getUserinfoFromHashMap(hashMap: HashMap<String,Any?>) : UserData{
        return UserData(
                hashMap["id"] as String,
                hashMap["timestamp"] as Long,
                hashMap["email"] as String,
                hashMap["profileUrl"] as String,
//                            snapshot.get("isAdmin") as Boolean,
//                            snapshot.get("isDeveloper") as Boolean,
//                            snapshot.get("isProvider") as Boolean,
                hashMap["admin"] as Boolean,
                hashMap["developer"] as Boolean,
                hashMap["provider"] as Boolean,
                hashMap["displayName"] as String,
                hashMap["firstName"] as String,
                hashMap["lastName"] as String,
                hashMap["schoolId"] as String,
                hashMap["schoolName"] as String,
                hashMap["gradeId"] as String,
                hashMap["grade"] as String,
                hashMap["classId"] as String,
                hashMap["class_"] as String
        )
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