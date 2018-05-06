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
import com.zaen.testly.fragments.cas.PreviewCardFragment.Companion.CARD
import java.util.ArrayList

abstract class CreateCasAdapter(private val casList: ArrayList<CasData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val CARD = 1
        const val SET  = 2
    }

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

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)


}