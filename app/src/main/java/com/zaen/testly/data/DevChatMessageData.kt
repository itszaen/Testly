package com.zaen.testly.data

import com.google.firebase.firestore.DocumentSnapshot


class DevChatMessageData (
        override var id: String,
        override var timestamp: Long,
        val text: String,
        val sender: UserData
) : FirebaseDocument(id,timestamp,CHAT){
    companion object {
        fun getDevChatMessageDataFromDocument(snapshot: DocumentSnapshot) : DevChatMessageData{
            val senderField = snapshot.data!!["sender"] as HashMap<String,Any?>
            return DevChatMessageData(
                    snapshot.get("id") as String,
                    snapshot.data!!["timestamp"] as Long,
                    snapshot.data!!["text"] as String,
                    UserData.getUserDataFromHashMap(senderField)
            )
        }
    }
}