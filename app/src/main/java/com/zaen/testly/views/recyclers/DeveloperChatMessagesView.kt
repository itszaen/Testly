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
import com.google.firebase.firestore.FirebaseFirestore
import com.zaen.testly.R
import kotlinx.android.synthetic.main.view_item_message_received.view.*
import java.util.ArrayList

class DeveloperChatMessagesView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "DeveloperChatMessages"
    }
    init {
        val inflater = LayoutInflater.from(getContext())
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        inflater.inflate(R.layout.recycler_dev_chat,null)
    }

    fun getMessages():ArrayList<Message>{
        var messageList = arrayListOf<Message>()
        // TODO Set archive date
        FirebaseFirestore.getInstance().collection("chats").document("dev").collection("messages")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        for (document in it.result){
                            Log.d(TAG, "Getting chat messages. ${document.id} -> ${document.data}")
                            document.data[""]
                            messageList.add()
                        }
                        // Set adapter with acquired data
                        this.adapter = MessageListAdapter(context,messageList)
                    } else {
                        Log.w(TAG,"Error getting chat message Documents. Exception: ${it.exception}")
                    }
                }
    }

    data class Message(val message:String, val sender:User, val createdAt: Long)
    data class User(val userId: String, val displayName:String, val profileUrl: Uri)

    class MessageReceivedHolder(view: View) : RecyclerView.ViewHolder(view){
//        interface ItemClickListener{
//            fun onItemClick(view: View, position: Int)
//        }
        val messageText: TextView = view.findViewById(R.id.text_message_received_body)
        val timestampText: TextView = view.findViewById(R.id.text_message_received_time)
        val nameText: TextView = view.findViewById(R.id.text_message_received_name)
        val profileImage: ImageView = view.findViewById(R.id.image_message_received_profile)

        fun bind(message: Message){
            messageText.text = message.message
            timestampText.text = message.createdAt.toString()
            nameText.text = message.sender.displayName
            profileImage.setImageURI(message.sender.profileUrl)
        }
    }

    class MessageSentHolder(view: View) : RecyclerView.ViewHolder(view){
        val messageText: TextView = view.findViewById(R.id.text_message_received_body)
        val timestampText: TextView = view.findViewById(R.id.text_message_received_time)

        fun bind(message: Message){
            messageText.text = message.message
            timestampText.text = message.createdAt.toString()
        }
    }

    class MessageListAdapter(private val context: Context,
                             private val mMessageList: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
            if (message.sender.userId == FirebaseAuth.getInstance().currentUser!!.uid){
                return MESSAGE_TYPE_SENT
            } else {
                return MESSAGE_TYPE_RECEIVED
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
}