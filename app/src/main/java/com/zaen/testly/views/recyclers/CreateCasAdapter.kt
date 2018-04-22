package com.zaen.testly.views.recyclers

import android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zaen.testly.Cas
import com.zaen.testly.data.DevChatMessageData
import com.zaen.testly.data.SetData
import com.zaen.testly.data.SpellingCardData
import java.util.ArrayList



class CreateCasAdapter(private val dataList: ArrayList<Cas>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CARD_SPELLING = 1
    private val CARD_VOCABULARY = 2
    private val SET = 3

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
        return dataList.size
    }

    override fun getItemViewType(position:Int):Int{
        val cas = ""
        return if (){
            CARD_SPELLING
        } else if () {
            CARD_VOCABULARY
        } else {
            SET
        }
    }
}