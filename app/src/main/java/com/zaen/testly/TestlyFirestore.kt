package com.zaen.testly

import android.util.Log
import com.google.firebase.firestore.*
import com.zaen.testly.utils.LogUtils

open class TestlyFirestore(val context: Any) {
    companion object {
    }

    interface DocumentListener{
        fun handleListener(listener: ListenerRegistration?)
        fun onDocumentUpdate(path: DocumentReference,snapshot: DocumentSnapshot?,exception: Exception?)
    }

    interface CollectionListener{
        fun handleListener(listener: ListenerRegistration?)
        fun onDocumentsUpdate(path: Query, snapshot: DocumentSnapshot?, exception: Exception?)
    }

    interface CollectionChangeListener{
        fun handleListener(listener: ListenerRegistration?)
        fun onFailure(exception: Exception?)
        fun onNewDocument(path: Query, snapshot: DocumentSnapshot)
        fun onModifyDocument(path: Query, snapshot: DocumentSnapshot)
        fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot)
    }

    interface UploadToCollectionListener{
        fun onDocumentUpload(path: Query, reference: DocumentReference?,exception: Exception?)
    }

    private var mDb :FirebaseFirestore? = null

    init {
        mDb = FirebaseFirestore.getInstance()
    }

    fun addDocumentToCollection(path: CollectionReference, data: Any, listener: UploadToCollectionListener){
        path.add(data)
                .addOnSuccessListener {
                    Log.d(LogUtils.TAG(context), "[Success] Uploading document($path) successful. Reference: $it")
                    listener.onDocumentUpload(path,it,null)
                }
                .addOnFailureListener{
                    Log.w(LogUtils.TAG(context), "[Failure] Uploading document($path) failed. Exception: $it")
                    listener.onDocumentUpload(path,null,it)
                }
    }

    fun addDocumentToCollection(path: CollectionReference, name:String, data: Any, listener: UploadToCollectionListener){
        path.document(name)
                .set(data)
                .addOnSuccessListener {
                    Log.d(LogUtils.TAG(context), "[Success] Uploading document($path) successful. Reference: ${path.document("name")}")
                    listener.onDocumentUpload(path,path.document("name"),null)
                }
                .addOnFailureListener{
                    Log.w(LogUtils.TAG(context), "[Failure] Uploading document($path) failed. Exception: $it")
                    listener.onDocumentUpload(path,null,it)
                }
    }

    fun getDocument(path: DocumentReference, listener: DocumentListener){
        path.get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val snapshot = it.result
                        if (snapshot.exists()){
                            Log.d(LogUtils.TAG(context),"[Success] Getting document($path). data:${snapshot.data}")
                            listener.onDocumentUpdate(path,snapshot,null)
                        } else {
                            Log.d(LogUtils.TAG(context),"[Failure] No document($path) found.")
                            listener.onDocumentUpdate(path,null,null)
                        }
                    } else {
                        Log.d(LogUtils.TAG(context),"[Failure] Error getting document($path). Exception:${it.exception}")
                        listener.onDocumentUpdate(path,null,it.exception)
                    }
                }
    }

    fun addDocumentListener(path: DocumentReference, listener: DocumentListener){
        var successful = false
        val registration = path.addSnapshotListener { snapshot, e ->
            if (e != null){
                Log.w(LogUtils.TAG(context), "[Failure] Listening to document($path). Exception: $e")
                listener.onDocumentUpdate(path,null,e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                successful = true
                Log.d(LogUtils.TAG(context), "[Success] Listening to document($path). Current data: ${snapshot.data}")
                listener.onDocumentUpdate(path,snapshot,null)
            } else {
                Log.d(LogUtils.TAG(context), "[Failure] Listening to document($path). Current data: null")
                listener.onDocumentUpdate(path,null,null)
            }
        }
        if (successful){
            listener.handleListener(registration)
        } else {
            listener.handleListener(null)
        }
    }

    fun getDocuments(path: Query, listener: CollectionListener){
        path.get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d(LogUtils.TAG(context),"[Success] Getting documents from collection($path).")
                        for (snapshot in it.result){
                            Log.d(LogUtils.TAG(context)," ${snapshot.id} : ${snapshot.data}")
                            listener.onDocumentsUpdate(path,snapshot,null)
                        }
                        Log.d(LogUtils.TAG(context),"[Success] End.($path)")
                    } else {
                        Log.d(LogUtils.TAG(context),"[Failure] Getting documents from collection($path) failed.")
                        listener.onDocumentsUpdate(path,null,it.exception)
                    }
                }
    }

    fun addCollectionListener(path: Query, listener: CollectionChangeListener){
        var successful = false
        val registration = path.addSnapshotListener { snapshots , e ->
            if (e != null || snapshots == null){
                Log.d(LogUtils.TAG(context),"[Failure] Listening to collection($path) failed. Exception: $e")
                listener.onFailure(e)
                return@addSnapshotListener
            }
            successful = true
            for (snapshotChange in snapshots.documentChanges){
                when (snapshotChange.type){
                    DocumentChange.Type.ADDED -> {
                        val snapshot = snapshotChange.document
                        Log.d(LogUtils.TAG(context),"[Success] Listening to collection($path).\nNew document:${snapshot.data}")
                        listener.onNewDocument(path,snapshot)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val snapshot = snapshotChange.document
                        Log.d(LogUtils.TAG(context),"[Success] Listening to collection($path).\nModified document:${snapshot.data}")
                        listener.onModifyDocument(path,snapshot)
                    }
                    DocumentChange.Type.REMOVED -> {
                        val snapshot = snapshotChange.document
                        Log.d(LogUtils.TAG(context),"[Success] Listening to collection($path).\nDeleted document:${snapshot.data}")
                        listener.onDeleteDocument(path,snapshot)
                    }
                }
            }
        }
        if (successful){
            listener.handleListener(registration)
        } else {
            listener.handleListener(null)
        }
    }

}