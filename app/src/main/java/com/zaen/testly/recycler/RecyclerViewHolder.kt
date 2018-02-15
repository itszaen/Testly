package com.zaen.testly.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.zaen.testly.R

/**
 * Created by zaen on 2/11/18.
 */
class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    val itemExamTitle :TextView = view.findViewById(R.id.exam_title)
    val itemExamSubjectTitle: TextView = view.findViewById(R.id.exam_subject_title)
//    val itemImageView: ImageView = view.findViewById(R.id.itemImageView)

    init {
        // layoutの初期設定するときはココ
    }

}