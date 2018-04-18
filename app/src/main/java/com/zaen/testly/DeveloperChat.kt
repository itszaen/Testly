package com.zaen.testly

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.utils.Common
import java.util.*

class DeveloperChat(context: Any){
    companion object {
        const val TAG = "DeveloperChat"
    }
    private var mListener: DeveloperChatListener? = null

    var messageList = arrayListOf<DevChatMessageData>()
    var latestMessageTime : Long = 0
    var isListening = false
    var storedMessages = arrayListOf<String>()

    val chatPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    interface DeveloperChatListener{
        fun onGotMessages()
    }

    init {
        if (context is DeveloperChatListener){
            mListener = context
        } else {
            throw Exception()
        }
    }

    fun downloadMessages(){
        // TODO Set archive date
        chatPath.collection("messages")
                .orderBy("timestampUnix")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        // Set adapter with acquired data
                        for (document in it.result.documents) {
                            storeMessage(document)
                        }
                        mListener?.onGotMessages()
                    } else {
                        Log.w(TAG,"Error getting chat message Documents. Exception: ${it.exception}")
                    }
                }
    }
    fun storeMessage(document: DocumentSnapshot){
        Log.d(TAG, "Getting chat messages. ${document.id} -> ${document.data}")
        // discard invalid message
        if (Common().allNotNull(
                        document.data?.get("sender"),
                        document.data?.get("text"),
                        document.data?.get("timestamp"),
                        document.data?.get("timestampUnix")
                )){
            Log.w(TAG, "Invalid Chat message. Id:${document.id}")
            return
        } else {
            val senderField = document.data!!["sender"] as ArrayList<*>
            val sender = DevChatUserData(
                    senderField[0] as String,
                    senderField[1] as String,
                    Uri.parse(senderField[2] as String)
            )
            val message = DevChatMessageData(
                    document.data!!["text"] as String,
                    sender,
                    document.data!!["timestampUnix"] as Long
            )

            if (storedMessages.contains(document.id)){
                messageList.set(storedMessages.indexOf(document.id),message)
            } else {
                messageList.add(message)
                storedMessages.add(document.id)
                latestMessageTime = document.data?.get("timestampUnix") as Long
            }
        }
    }
    fun listenToMessages(){
        chatPath.collection("messages")
                .orderBy("timestampUnix")
                .whereGreaterThan("timestampUnix",latestMessageTime)
                .addSnapshotListener{ query: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                    if (exception != null){
                        Log.w(TAG,"Listen on Message failed. Exception: $exception")
                    }
                    for (document in query!!.documents){
                            storeMessage(document)
                    }
                    isListening = true
                    mListener?.onGotMessages()
                }
    }
    fun uploadMessage(text: String, user: FirebaseUser){
        val timestamp = FieldValue.serverTimestamp()
        val timestampUnix: Long = System.currentTimeMillis() / 1000L
        val uid = user.uid
        val displayName = user.displayName!!
        val photoUrl = user.photoUrl.toString()

        val sender = listOf(uid,displayName,photoUrl)

        val document = HashMap<String,Any?>()
        document["text"] = text
        document["timestamp"] = timestamp
        document["timestampUnix"] = timestampUnix
        document["sender"] = sender

        chatPath.collection("messages")
                .add(document)
                .addOnSuccessListener {
                    Log.d(TAG,"Developer Chat document -> Firestore success. Added with id: ${it.id}")
                }
                .addOnFailureListener {
                    Log.w(TAG,"Developer chat upload message failed. Exception: $it")
                }


    }
}