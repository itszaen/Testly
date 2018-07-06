package com.zaen.testly.main.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.TestlyUser
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.data.UserData
import com.zaen.testly.devchat.DeveloperChat
import com.zaen.testly.views.recyclers.DeveloperChatAdapter
import kotlinx.android.synthetic.main.fragment_dev_chat.*

class DeveloperChatFragment : BaseFragment(){

    private var mDevChat = DeveloperChat(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (savedInstanceState != null){

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?  {
        layoutRes = R.layout.fragment_dev_chat
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkTextField(edit_chatbox.text.toString())
        mDevChat.listenToMessages(object: DeveloperChat.DeveloperChatListener{
            override fun onMessage() {
                recycler_dev_chat.apply{
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL,false)
                    adapter = DeveloperChatAdapter(mDevChat.messageList)
                }
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){

        }
        initializeViews()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.put
    }

    private fun initializeViews(){
        edit_chatbox.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) { checkTextField(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { checkTextField(s.toString()) }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { checkTextField(s.toString()) }
        })



        button_chatbox_send.setOnClickListener{
            // Upload Message
            val messageText = edit_chatbox.text.toString()
            TestlyUser(this).addUserinfoListener(object: TestlyUser.UserinfoListener{
                override fun onUserinfoUpdate(userinfo: UserData?) {
                    if (userinfo != null){
                        mDevChat.uploadMessage(messageText, userinfo)
                    }
                }
            })
            // Clear EditText
            edit_chatbox.text.clear()
        }
    }

    fun checkTextField(text: String?){
        if (text != null && text.toString() != ""){
            button_chatbox_send.isEnabled = true
            initializeViews()
        } else {
            button_chatbox_send.setOnClickListener(null)
            button_chatbox_send.isEnabled = false
        }
    }
}
