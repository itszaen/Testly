package com.zaen.testly.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.zaen.testly.DeveloperChat
import com.zaen.testly.R
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.utils.Common
import com.zaen.testly.views.recyclers.DeveloperChatMessagesView
import kotlinx.android.synthetic.main.fragment_dev_chat.*

class DeveloperChatFragment : Fragment() {
    companion object {
        const val TAG = "DeveloperChatFrag"
    }
    var activity: Activity? = null
    private var unbinder: Unbinder? = null

    var chatPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        val view = inflater.inflate(R.layout.fragment_dev_chat,container,false)
        unbinder = ButterKnife.bind(this,view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
        val mChat = DeveloperChat(this)

        // Listen to New messages
        chatPath.collection("messages")
                .addSnapshotListener{documents:QuerySnapshot,exception:FirebaseFirestoreException? ->
                    if (exception != null){
                        Log.w(TAG,"Listen on Message failed. Exception: $exception")
                        return@addSnapshotListener
                    }
                    mChat.storeMessages(documents,)

                }
    }


    @OnTextChanged(R.id.edit_chatbox)
    fun onTextChanged(text: CharSequence?){
        button_chatbox_send.isClickable = text != null && text != ""
    }

    @Optional
    @OnClick(R.id.button_chatbox_send)
    fun onSendMessage(view: View){
        // Clear EditText
        edit_chatbox.text.clear()

        // Upload Message
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val messageText = edit_chatbox.text.toString()
        val timestamp = FieldValue.serverTimestamp()
        val uid = firebaseUser.uid
        val displayName = firebaseUser.displayName!!
        val photoUrl = firebaseUser.photoUrl.toString()

        val sender = listOf(uid,displayName,photoUrl)

        val document = HashMap<String,Any?>()
        document["text"] = messageText
        document["timestamp"] = timestamp
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
