package com.zaen.testly.Recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zaen.testly.R

/**
 * Created by zaen on 2/11/18.
 */
class RecyclerAdapter(private val context: Context,
                      private val itemClickListener: RecyclerViewHolder.ItemClickListener,
                      private val itemList:List<String>)
    : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mRecyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
        holder?.let {
            it.itemTextView.text = itemList.get(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.card_exam, parent, false)

        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return RecyclerViewHolder(mView)
    }

}