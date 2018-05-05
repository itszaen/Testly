package com.zaen.testly.views.recyclers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zaen.testly.R
import com.zaen.testly.data.CardData
import com.zaen.testly.data.CasData
import com.zaen.testly.data.SetData
import java.util.ArrayList

class CreateCasGridAdapter(private val casList: ArrayList<CasData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CARD = 1
    private val SET = 2

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
        return casList.size
    }

    override fun getItemViewType(position:Int):Int{
        val cas = casList[position]
        return if (cas is CardData){
            CARD
        } else {
            SET
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        val inflater = LayoutInflater.from(parent.context)
        when(viewType){
            CARD     -> {
                view = inflater.inflate(R.layout.item_message_sent,parent,false)
                return CreateCasGridAdapter.CardHolder(view)
            }
            SET -> {
                view = inflater.inflate(R.layout.view_item_message_received,parent,false)
                return CreateCasGridAdapter.SetHolder(view)
            }
        }
        return CreateCasGridAdapter.SetHolder(view as View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cas = casList[position]
        when(holder.itemViewType){
            CARD -> (holder as CardHolder).bind(cas as CardData)
            SET  -> (holder as SetHolder ).bind(cas as SetData)
        }
    }
    class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        private val titleText: TextView = view.findViewById(R.id.title_item_grid_create_card)
        fun bind(card: CardData){
            titleText.text = card.id
        }
    }

    class SetHolder(view: View) : RecyclerView.ViewHolder(view){
        private val titleText: TextView = view.findViewById(R.id.title_item_grid_create_set)
        fun bind(set: SetData){
            titleText.text = set.title
        }
    }


}