package com.zaen.testly.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import butterknife.Optional
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.DeveloperChat
import com.zaen.testly.R
import com.zaen.testly.TestlyUser
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.views.recyclers.DeveloperChatAdapter
import kotlinx.android.synthetic.main.fragment_dev_chat.*

class DeveloperChatFragment : BaseFragment(){

    private var mDevChat = DeveloperChat(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }

        mDevChat = DeveloperChat(this)
        mDevChat.listenToMessages(object: DeveloperChat.DeveloperChatListener{
            override fun onMessage() {
                // First time (download) call listener
                if (mDevChat.storedDocuments.size > 0) {
                    recycler_dev_chat.adapter = DeveloperChatAdapter(mDevChat.messageList)
                }
            }
        })
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
            layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL,false)
            adapter = DeveloperChatAdapter(mDevChat.messageList)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        val messageText = edit_chatbox.text.toString()
        mDevChat.uploadMessage(messageText, TestlyUser(this).currentUser!!)

        // Clear EditText
        edit_chatbox.text.clear()
    }
}
