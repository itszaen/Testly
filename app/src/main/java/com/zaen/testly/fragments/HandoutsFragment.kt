package com.zaen.testly.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.R

/**
 * Created by zaen on 2/26/18.
 */
class HandoutsFragment : FileBrowserFragment() {
    companion object {
        const val TAG = "HandoutsFragment"
        const val bucket = "testly-2014.appspot.com"
        const val sub = "subdirectories"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_handouts
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Firebase Documents
        val db = FirebaseFirestore.getInstance()
        db.collection("storage").document(bucket).collection(sub).document("subjects").collection(sub)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                    }
                }
    }

}