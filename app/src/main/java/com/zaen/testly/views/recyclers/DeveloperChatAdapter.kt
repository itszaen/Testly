package com.zaen.testly.views.recyclers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.R
import com.zaen.testly.data.DevChatMessageData
import java.util.ArrayList

class DeveloperChatAdapter(private val mMessageList: ArrayList<DevChatMessageData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
}