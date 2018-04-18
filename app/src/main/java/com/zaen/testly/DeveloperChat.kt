package com.zaen.testly

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.fragments.DeveloperChatFragment
import com.zaen.testly.utils.Common

class DeveloperChat(context: Any){
    companion object {
        const val TAG = "DeveloperChat"
    }
    private var mListener: DeveloperChatListener? = null
    var messageList = arrayListOf<DevChatMessageData>()

    val chatPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    interface DeveloperChatListener{
        fun onGotMessages()
    }

    init {
        if (context is DeveloperChatListener){
            mListener = context
        }
    }

    fun downloadMessages(){
        // TODO Set archive date
        chatPath.collection("messages")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        // Set adapter with acquired data
                        for (document in it.result.documents) {
                            storeMessage(document)
                        }
                        mListener?.onGotMessages()
                    } else {
                        Log.w(DeveloperChatMessagesView.TAG,"Error getting chat message Documents. Exception: ${it.exception}")
                    }
                }
    }
    fun storeMessage(document: DocumentSnapshot){
        Log.d(TAG, "Getting chat messages. ${document.id} -> ${document.data}")
        // discard invalid message
        if (Common().allNotNull(
                        document.data?.get("sender"),
                        document.data?.get("text"),
                        document.data?.get("timestamp")
                )){
            Log.w(TAG, "Invalid Chat message. Id:${document.id}")
            return
        } else {
            val senderField = document.data!!["sender"] as Map<*, *>
            val sender = DevChatUserData(
                    senderField["userId"] as String,
                    senderField["displayName"] as String,
                    Uri.parse(senderField["profileUrl"] as String)
            )
            val message = DevChatMessageData(
                    document.data!!["text"] as String,
                    sender,
                    document.data!!["timestamp"] as FieldValue
            )
            messageList.add(message)
        }
    }
    fun listenToMessages(){
        chatPath.collection("messages")
                .addSnapshotListener{ query: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                    if (exception != null){
                        Log.w(TAG,"Listen on Message failed. Exception: $exception")
                    }
                    for (document in query!!.documents){
                        storeMessage(document)
                    }
                    mListener?.onGotMessages()
                }
    }
    fun uploadMessage(text: String, user: FirebaseUser){
        val timestamp = FieldValue.serverTimestamp()
        val uid = user.uid
        val displayName = user.displayName!!
        val photoUrl = user.photoUrl.toString()

        val sender = listOf(uid,displayName,photoUrl)

        val document = HashMap<String,Any?>()
        document["text"] = text
        document["timestamp"] = timestamp
        document["sender"] = sender

        chatPath.collection("messages")
                .add(document)
                .addOnSuccessListener {
                    Log.d(DeveloperChatFragment.TAG,"Developer Chat document -> Firestore success. Added with id: ${it.id}")
                }
                .addOnFailureListener {
                    Log.w(DeveloperChatFragment.TAG,"Developer chat upload message failed. Exception: $it")
                }


    }
}