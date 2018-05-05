package com.zaen.testly

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.utils.LogUtils

open class TestlyFirestore(val context: Any) {
    companion object {
    }

    interface DocumentListener{
        fun onDocumentUpdate(path: DocumentReference,snapshot: DocumentSnapshot?)
    }

    interface UploadToCollectionListener{
        fun onDocumentUpload(path: CollectionReference, reference: DocumentReference?)
    }

    private var mDb :FirebaseFirestore? = null

    init {
        mDb = FirebaseFirestore.getInstance()
    }

    fun addDocumentToCollection(path: CollectionReference, data: Any, listener: UploadToCollectionListener){
        path.add(data)
                .addOnSuccessListener {
                    Log.d(LogUtils.TAG(context), "Uploading document($path) successful. Reference: $it")
                    listener.onDocumentUpload(path,it)
                }
                .addOnFailureListener{
                    Log.w(LogUtils.TAG(context), "Uploading document($path) failed. Exception: $it")
                    listener.onDocumentUpload(path,null)
                }
    }

    fun addDocumentListener(path: DocumentReference, listener: DocumentListener){
        path.addSnapshotListener { snapshot, e ->
            if (e != null){
                Log.w(LogUtils.TAG(context), "Listening to document($path) failed. Exception: $e")
                listener.onDocumentUpdate(path,null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(LogUtils.TAG(context), "Listening to document($path). Current data: ${snapshot.data}")
                listener.onDocumentUpdate(path,snapshot)
            } else {
                Log.d(LogUtils.TAG(context), "Listening to document($path). Current data: null")
                listener.onDocumentUpdate(path,null)
            }
        }
    }

    fun addCollectionListener(path: CollectionReference){

    }

}