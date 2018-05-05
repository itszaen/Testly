package com.zaen.testly

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.utils.Common
import com.zaen.testly.utils.LogUtils

class DeveloperChat(context: Any){
    companion object {
    }
    private var mListener: DeveloperChatListener? = null

    var registration: ListenerRegistration? = null

    var messageList = arrayListOf<DevChatMessageData>()
//    var latestMessageTime : Long = 0
//    var isListening = false
    var storedDocuments = arrayListOf<String>()

    val chatDocumentPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    interface DeveloperChatListener{
        fun onMessage()
    }

    init {
        if (context is DeveloperChatListener){
            mListener = context
        } else {
            throw Exception()
        }
    }

    fun listenToMessages(listener: DeveloperChatListener){
        // TODO Set archive date
        TestlyFirestore(this).addCollectionListener(chatDocumentPath.collection("messages").orderBy("timestamp"),
                object: TestlyFirestore.CollectionChangeListener{
                    override fun handleListener(listener: ListenerRegistration?) {
                        registration = listener
                    }

                    override fun onFailure(exception: Exception?) {
                        registration = null
                    }

                    override fun onNewDocument(path: Query, snapshot: DocumentSnapshot) {
                        storeMessage(snapshot)
                        listener.onMessage()
                    }

                    override fun onModifyDocument(path: Query, snapshot: DocumentSnapshot) {
                        val message = getMessageFromDocument(snapshot)
                        if (message != null){
                            messageList[storedDocuments.indexOf(snapshot.id)] = message
                        }
                        listener.onMessage()
                    }

                    override fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot) {
                        val message = getMessageFromDocument(snapshot)
                        messageList.remove(message)
                        listener.onMessage()
                    }

                })
//        chatDocumentPath.collection("messages")
//                .orderBy("timestamp")
//                .get()
//                .addOnCompleteListener {
//                    if (it.isSuccessful){
//                        // Set adapter with acquired data
//                        for (snapshot in it.result.documents) {
//                            storeMessage(snapshot)
//                        }
//                        mListener?.onGotMessages()
//                    } else {
//                        Log.w(LogUtils.TAG(this),"Error getting chat message Documents. Exception: ${it.exception}")
//                    }
//                }
    }
    fun storeMessage(snapshot: DocumentSnapshot){
        Log.d(LogUtils.TAG(this), "Getting chat messages. ${snapshot.id} -> ${snapshot.data}")
        val message = getMessageFromDocument(snapshot)
        if (message != null){
//        {
//            if (storedDocuments.contains(snapshot.id)){
//                messageList[storedDocuments.indexOf(snapshot.id)] = message
//            } else {
                messageList.add(message)
                storedDocuments.add(snapshot.id)
//                latestMessageTime = snapshot.data?.get("timestamp") as Long
        }
    }
//    fun listenToMessages(){
//        chatDocumentPath.collection("messages")
//                .orderBy("timestamp")
//                .whereGreaterThan("timestamp",latestMessageTime)
//                .addSnapshotListener{ query: QuerySnapshot?, exception: FirebaseFirestoreException? ->
//                    if (exception != null){
//                        Log.w(LogUtils.TAG(this),"Listen on Message failed. Exception: $exception")
//                    }
//                    for (snapshot in query!!.documents){
//                            storeMessage(snapshot)
//                    }
//                    isListening = true
//                    mListener?.onGotMessages()
//                }
//    }
    fun uploadMessage(text: String, user: FirebaseUser){
        val timestamp: Long = System.currentTimeMillis() / 1000L
        val uid = user.uid
        val displayName = user.displayName!!
        val email = user.email!!
        val photoUrl = user.photoUrl.toString()

        val sender = DevChatUserData(uid,displayName,email,photoUrl)
        val message = DevChatMessageData(text,sender,timestamp)

        TestlyFirestore(this).addDocumentToCollection(chatDocumentPath.collection("messages"),message,object: TestlyFirestore.UploadToCollectionListener{
            override fun onDocumentUpload(path: Query, reference: DocumentReference?, exception: Exception?) {
                if (reference == null){
                    // error
                }
            }
        })
    }

    fun getMessageFromDocument(snapshot: DocumentSnapshot):DevChatMessageData?{
        if (Common().allNotNull(
                        snapshot.data?.get("sender"),
                        snapshot.data?.get("text"),
                        snapshot.data?.get("timestamp")
                )){
            Log.w(LogUtils.TAG(this), "[Failure] Invalid Chat message. Id:${snapshot.id}")
            return null
        }
        val senderField = snapshot.data!!["sender"] as HashMap<String,*>
        val sender = DevChatUserData(
                senderField["id"] as String,
                senderField["displayName"] as String,
                senderField["email"] as String,
                senderField["profileUrl"] as String
        )
        val message = DevChatMessageData(
                snapshot.data!!["text"] as String,
                sender,
                snapshot.data!!["timestamp"] as Long
        )
        return message
    }
}