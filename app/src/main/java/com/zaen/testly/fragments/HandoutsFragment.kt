package com.zaen.testly.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.zaen.testly.R
import com.zaen.testly.activities.ImageViewActivity

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
        return inflater.inflate(R.layout.fragment_handouts,container,false)
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