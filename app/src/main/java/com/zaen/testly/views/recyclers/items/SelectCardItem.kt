package com.zaen.testly.views.recyclers.items

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.zaen.testly.R
import com.zaen.testly.data.CardData
import com.zaen.testly.utils.CommonUtils
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.SimpleDateFormat
import java.util.*

class SelectCardItem(val card: CardData) : AbstractFlexibleItem<FlexibleViewHolder>() {
    fun withIcon(icon: Drawable) : SelectCardItem{
        //this.icon = icon
        return this
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return card.id.hashCode()
    }

    override fun getLayoutRes(): Int{
        return R.layout.item_recycler_grid_create_card
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): ViewHolder {
        return ViewHolder(view,adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: FlexibleViewHolder?, position: Int, payloads: MutableList<Any>?) {
        if (holder is ViewHolder){
            holder.titleText?.text = card.title
            holder.titleText?.isEnabled = true
            holder.dateText?.text = SimpleDateFormat("MMM d, yyyy", Locale.US).format(Date(card.timestamp*1000L)).toString()
        }
    }

    class ViewHolder(val view: View?, val adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) : FlexibleViewHolder(view,adapter) {
        val titleText: TextView? = view?.findViewById(R.id.title_item_grid_create_card)
        val dateText: TextView? = view?.findViewById(R.id.text_date_grid_create_card)
        override fun onClick(view: View?) {
            super.onClick(view)
        }

        override fun getActivationElevation(): Float {
            return CommonUtils().dpToPx(itemView.context, 4f).toFloat()
        }
    }
}