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

        mDevChat = DeveloperChat(this)

        recycler_dev_chat.layoutManager = LinearLayoutManager(activity)
        recycler_dev_chat.adapter = DeveloperChatAdapter(mDevChat.messageList)

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
        mDevChat.uploadMessage(messageText,firebaseUser)
    }
    override fun onGotMessages() {

    }
}
