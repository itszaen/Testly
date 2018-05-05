package com.zaen.testly

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class TestlyUser(val context: Any){
    companion object {
    }

    interface UserinfoListener{
        fun onUserinfoUpdate(snapshot:DocumentSnapshot?)
    }

    private var currentUser: FirebaseUser? = null

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
            override fun onDocumentUpdate(path: DocumentReference, snapshot: DocumentSnapshot?) {
                userinfoSnapshot = snapshot
                listener.onUserinfoUpdate(snapshot)
            }
        })
    }
}