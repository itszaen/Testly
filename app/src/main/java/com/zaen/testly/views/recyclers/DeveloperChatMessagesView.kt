package com.zaen.testly.views.recyclers

import android.content.Context
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.DeveloperChat
import com.zaen.testly.R
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.DevChatUserData
import com.zaen.testly.utils.Common
import kotlinx.android.synthetic.main.view_item_message_received.view.*
import java.util.ArrayList

class DeveloperChatMessagesView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr),
    DeveloperChat.DeveloperChatListener{

    companion object {
        const val TAG = "DeveloperChatMessages"
        val mDevChat = DeveloperChat(this)
    }
    init {
        val inflater = LayoutInflater.from(getContext())
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        inflater.inflate(R.layout.recycler_dev_chat,null)
        mDevChat.getMessages()
    }

    class MessageReceivedHolder(view: View) : RecyclerView.ViewHolder(view){
        val messageText: TextView = view.findViewById(R.id.text_message_received_body)
        val timestampText: TextView = view.findViewById(R.id.text_message_received_time)
        val nameText: TextView = view.findViewById(R.id.text_message_received_name)
        val profileImage: ImageView = view.findViewById(R.id.image_message_received_profile)

        fun bind(message: DevChatMessageData){
            messageText.text = message.message
            timestampText.text = message.createdAt.toString()
            nameText.text = message.sender.displayName
            profileImage.setImageURI(message.sender.profileUrl)
        }
    }

    class MessageSentHolder(view: View) : RecyclerView.ViewHolder(view){
        val messageText: TextView = view.findViewById(R.id.text_message_received_body)
        val timestampText: TextView = view.findViewById(R.id.text_message_received_time)

        fun bind(message: DevChatMessageData){
            messageText.text = message.message
            timestampText.text = message.createdAt.toString()
        }
    }

    class MessageListAdapter(private val context: Context,
                             private val mMessageList: ArrayList<DevChatMessageData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        val MESSAGE_TYPE_RECEIVED = 1
        val MESSAGE_TYPE_SENT = 2

        private var mRecyclerView: RecyclerView? = null

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            mRecyclerView = recyclerView
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            mRecyclerView = null

        }

        override fun getItemCount():Int{
            return mMessageList.size
        }

        override fun getItemViewType(position:Int):Int{
            val message = mMessageList[position]
            return if (message.sender.userId == FirebaseAuth.getInstance().currentUser!!.uid){
                MESSAGE_TYPE_SENT
            } else {
                MESSAGE_TYPE_RECEIVED
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: View? = null
            val inflater = LayoutInflater.from(parent.context)
            when(viewType){
                MESSAGE_TYPE_SENT     -> {
                    view = inflater.inflate(R.layout.view_item_message_sent,parent,false)
                    return MessageSentHolder(view)
                }
                MESSAGE_TYPE_RECEIVED -> {
                    view = inflater.inflate(R.layout.view_item_message_received,parent,false)
                    return MessageReceivedHolder(view)
                }
            }
            return MessageSentHolder(view as View)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val message = mMessageList[position]
            when(holder.itemViewType) {
                MESSAGE_TYPE_SENT -> (holder as MessageSentHolder).bind(message)
                MESSAGE_TYPE_RECEIVED -> (holder as MessageReceivedHolder).bind(message)
            }
        }


    }

    override fun onGotMessages(messageList: ArrayList<DevChatMessageData>) {
        this.adapter = DeveloperChatMessagesView.MessageListAdapter(context, messageList)
    }
}