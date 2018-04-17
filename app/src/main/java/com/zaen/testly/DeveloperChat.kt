package com.zaen.testly

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.utils.Common
import com.zaen.testly.views.recyclers.DeveloperChatMessagesView
import java.util.ArrayList

class DeveloperChat(context: Any){
    private var mListener: DeveloperChatListener? = null

    interface DeveloperChatListener{
        fun onGotMessages(messageList: ArrayList<DevChatMessageData>)
    }

    init {
        if (context is DeveloperChatListener){
            mListener = context
        }
    }

    fun getMessages(){
        val messageList = arrayListOf<DevChatMessageData>()
        // TODO Set archive date
        FirebaseFirestore.getInstance().collection("chats").document("dev").collection("messages")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){

                        // Set adapter with acquired data
                        mListener?.onGotMessages(messageList)
                    } else {
                        Log.w(DeveloperChatMessagesView.TAG,"Error getting chat message Documents. Exception: ${it.exception}")
                    }
                }
    }
    fun storeMessages(documents: List<DocumentSnapshot>, messageList: List<DevChatMessageData>, TAG: String){
        for (document in documents){
            Log.d(DeveloperChatMessagesView.TAG, "Getting chat messages. ${document.id} -> ${document.data}")
            // discard invalid message
            if (Common().allNotNull(
                            document.data?.get("sender"),
                            document.data?.get("text"),
                            document.data?.get("timestamp")
                    )){ break } else {
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
    }
}