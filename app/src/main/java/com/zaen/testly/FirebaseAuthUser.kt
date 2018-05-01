package com.zaen.testly

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.utils.LogUtils

class FirebaseAuthUser(context: Any){
    companion object {
    }

    interface UserinfoListener{
        fun onUserinfoUpdate(snapshot:DocumentSnapshot?)
    }

    private var mListener: UserinfoListener? = null

    private var currentUser: FirebaseUser? = null

    // Firestore
    private var userCollectionRef = FirebaseFirestore.getInstance().collection("users")
    var userinfoSnapshot: DocumentSnapshot? = null

    init {
        if (context is UserinfoListener){
            mListener = context
        }
        currentUser = FirebaseAuth.getInstance()?.currentUser
    }

    fun isSignedIn():Boolean{
        return currentUser != null
    }

    fun listenUserinfo(){
        userCollectionRef.document(FirebaseAuth.getInstance().currentUser!!.uid)
                .addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w(LogUtils.TAG(this), "Userinfo listen failed. Exception: $exception")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(LogUtils.TAG(this), "Userinfo listened. Current data: ${snapshot.data}")
                userinfoSnapshot = snapshot
                mListener?.onUserinfoUpdate(snapshot)
            } else {
                Log.d(LogUtils.TAG(this), "Userinfo listened. Current data: null")
                userinfoSnapshot = null
                mListener?.onUserinfoUpdate(null)
            }
        }
    }
}