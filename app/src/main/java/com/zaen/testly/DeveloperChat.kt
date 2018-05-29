package com.zaen.testly

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.zaen.testly.R.mipmap.email
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.UserData
import com.zaen.testly.utils.Common
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
        // TODO Set archive date
        TestlyFirestore(this).addCollectionListener(chatDocumentPath.collection("messages").orderBy("timestamp"),
                object: TestlyFirestore.CollectionChangeListener{
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
                            // TODO check if this works
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
                storedDocuments.add(snapshot)
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
    fun uploadMessage(text: String, userinfo: UserData){
    val path = chatDocumentPath.collection("messages").document()
    val timestamp: Long = Common().getTimestamp()
    val message = DevChatMessageData(path.id,timestamp,text,userinfo)
        TestlyFirestore(this).addDocumentToCollection(path,message,object: TestlyFirestore.UploadToCollectionListener{
            override fun onDocumentUpload(path: Query, reference: DocumentReference?, exception: Exception?) {
                if (reference == null){
                    // error
                }
            }
        })
    }

    fun getMessageFromDocument(snapshot: DocumentSnapshot):DevChatMessageData?{
        if (Common().allNotNull(
                        snapshot.data?.get("id"),
                        snapshot.data?.get("timestamp"),
                        snapshot.data?.get("sender"),
                        snapshot.data?.get("text")
                )){
            Log.w(LogUtils.TAG(this), "[Failure] Invalid Chat message. Id:${snapshot.id}")
            return null
        }
        val senderField = snapshot.data!!["sender"] as HashMap<String,*>
        val sender = TestlyUser(this).getUserinfoFromDocument(snapshot)
        return DevChatMessageData.getDevChatMessageDataFromDocument(snapshot)
    }
}