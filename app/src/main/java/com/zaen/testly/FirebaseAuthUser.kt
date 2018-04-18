package com.zaen.testly

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthUser{
    companion object {
        const val TAG = "FirebaseAuthUser"
    }

    private var currentUser: FirebaseUser? = null

    init {
        currentUser = FirebaseAuth.getInstance()?.currentUser
    }

    fun isSignedIn():Boolean{
        return currentUser != null
    }
}