package com.zaen.testly.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.zaen.testly.DeveloperChat
import com.zaen.testly.R
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.views.recyclers.DeveloperChatAdapter
import kotlinx.android.synthetic.main.fragment_dev_chat.*

class DeveloperChatFragment : BaseFragment(),
        DeveloperChat.DeveloperChatListener{

    private var mDevChat = DeveloperChat(this)
    private var onGotMessageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }

        mDevChat = DeveloperChat(this)
        mDevChat.downloadMessages()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_dev_chat
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextField(edit_chatbox.text.toString())

        recycler_dev_chat.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL,true)
            adapter = DeveloperChatAdapter(mDevChat.messageList)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity()
        if (savedInstanceState != null){

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.put
    }

    @Optional
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
