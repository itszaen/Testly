package com.zaen.testly.devchat

import android.util.Log
import com.google.firebase.firestore.*
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.UserData
import com.zaen.testly.utils.CommonUtils
import com.zaen.testly.utils.LogUtils

class DeveloperChat(val context: Any){
    companion object {
    }

    var registration: ListenerRegistration? = null

    var messageList = arrayListOf<DevChatMessageData>()
    var storedDocuments = arrayListOf<DocumentSnapshot>()

    val chatDocumentPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    interface DeveloperChatListener{
        fun onMessage()
    }

    init {
    }

    fun listenToMessages(listener: DeveloperChatListener){
        // TODO [C1] Set archive date
        TestlyFirestore(this).addCollectionListener(chatDocumentPath.collection("messages").orderBy("timestamp"),
                object: TestlyFirestore.CollectionChangeListener {
                    override fun handleListener(registration: ListenerRegistration?) {
                        this@DeveloperChat.registration = registration
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
                            // [B1] TODO check if this works
                            // compare snapshot
                            storedDocuments[storedDocuments.indexOf(snapshot)] = snapshot
                            messageList[storedDocuments.indexOf(snapshot)] = message
                        }
                        listener.onMessage()
                    }

                    override fun onDeleteDocument(path: Query, snapshot: DocumentSnapshot) {
                        val message = getMessageFromDocument(snapshot)
                        messageList.remove(message)
                        listener.onMessage()
                    }

                })
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
                storedDocuments.add(snapshot)
//                latestMessageTime = snapshot.data?.get("timestamp") as Long
        }
    }

    fun uploadMessage(text: String, userinfo: UserData){
        val path = chatDocumentPath.collection("messages").document()
        val timestamp: Long = CommonUtils.getTimestamp()
        val message = DevChatMessageData(path.id,timestamp,text,userinfo)
        TestlyFirestore(this).addDocumentToCollection(path,message,object: TestlyFirestore.UploadToCollectionListener {
            override fun onDocumentUpload(path: Query, reference: DocumentReference?, exception: Exception?) {
                if (reference == null){
                    // error
                }
            }
        })
    }

    fun getMessageFromDocument(snapshot: DocumentSnapshot):DevChatMessageData?{
        if (CommonUtils.allNotNull(
                        snapshot.data?.get("id"),
                        snapshot.data?.get("timestamp"),
                        snapshot.data?.get("sender"),
                        snapshot.data?.get("text")
                )){
            Log.w(LogUtils.TAG(this), "[Failure] Invalid Chat message. Id:${snapshot.id}")
            return null
        }
        return DevChatMessageData.getDevChatMessageDataFromDocument(snapshot)
    }
}