package com.zaen.testly.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.zaen.testly.DeveloperChat
import com.zaen.testly.R
import com.zaen.testly.views.recyclers.DeveloperChatAdapter
import kotlinx.android.synthetic.main.fragment_dev_chat.*

class DeveloperChatFragment : Fragment(),
        DeveloperChat.DeveloperChatListener{
    companion object {
        const val TAG = "DeveloperChatFrag"
    }

    var activity: Activity? = null
    private var unbinder: Unbinder? = null
    private var mDevChat = DeveloperChat(this)
    private var onGotMessageCount = 0

    var chatPath = FirebaseFirestore.getInstance().collection("chats").document("dev")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDevChat = DeveloperChat(this)
        mDevChat.downloadMessages()

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        val view = inflater.inflate(R.layout.fragment_dev_chat,container,false)
        unbinder = ButterKnife.bind(this,view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextField(edit_chatbox.text.toString())

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        recycler_dev_chat.layoutManager = layoutManager

//        recycler_dev_chat.adapter = DeveloperChatAdapter(mDevChat.messageList)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()


    }


    @OnTextChanged(R.id.edit_chatbox)
    fun onTextChanged(text: CharSequence?){
        checkTextField(text.toString())
    }

    fun checkTextField(text: String?){
        if (text != null && text.toString() != ""){
            button_chatbox_send.isEnabled = true
            ButterKnife.bind(this,button_chatbox_send)
        } else {
            button_chatbox_send.setOnClickListener(null)
            button_chatbox_send.isEnabled = false
        }
    }

    @Optional
    @OnClick(R.id.button_chatbox_send)
    fun onSendMessage(view: View){
        // Upload Message
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val messageText = edit_chatbox.text.toString()
        mDevChat.uploadMessage(messageText,firebaseUser)

        // Clear EditText
        edit_chatbox.text.clear()
    }
    override fun onGotMessages() {
        // First time (download) call listener
        if (!mDevChat.isListening) {
            mDevChat.listenToMessages()
        }
        if (onGotMessageCount > 0) {
            recycler_dev_chat.adapter = DeveloperChatAdapter(mDevChat.messageList)
        }
        onGotMessageCount += 1
    }
}
