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
import java.text.SimpleDateFormat
import java.util.*

class CreateCasLinearAdapter(private val casList: ArrayList<CasData>) : CreateCasAdapter(casList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            CARD -> {
                view = inflater.inflate(R.layout.item_recycler_linear_create_card, parent, false)
                return CardHolder(view)
            }
            SET -> {
                view = inflater.inflate(R.layout.item_recycler_linear_create_set, parent, false)
                return SetHolder(view)
            }
        }
        return SetHolder(view as View)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cas = casList[position]
        when (holder.itemViewType) {
            CARD -> (holder as CardHolder).bind(cas as CardData)
            SET ->  (holder as SetHolder ).bind(cas as SetData)
        }
    }

    class CardHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.title_item_linear_create_card)
        private val dateText: TextView = view.findViewById(R.id.text_date_linear_create_card)
        fun bind(card: CardData) {
            titleText.text = card.title
            dateText.text = SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date(card.timestamp*1000L)).toString()
        }
    }

    class SetHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.title_item_linear_create_set)
        fun bind(set: SetData) {
            titleText.text = set.title

        }
    }
}