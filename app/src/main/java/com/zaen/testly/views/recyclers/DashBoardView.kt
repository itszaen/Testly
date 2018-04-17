package com.zaen.testly.views.recyclers

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zaen.testly.R
import java.util.*

/**
 * Created by zaen on 2/25/18.
 */
class DashBoardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr)
{

    val barList: ArrayList<ArrayList<String>> = readFileData()

    init {
        val inflater = LayoutInflater.from(getContext())
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        inflater.inflate(R.layout.view_dashboard,null)
        this.adapter = RecyclerAdapter(context, object: RecyclerViewHolder.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                //Toast.makeText(context, "position $position was tapped", Toast.LENGTH_SHORT).show()
            }
        },barList)

    }

    fun readFileData(): ArrayList<ArrayList<String>> {
        val scan = Scanner(getResources().openRawResource(R.raw.list1))
        val list: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

        while (scan.hasNextLine()) {
            val line = scan.nextLine()
            val parts = ArrayList<String>(line.split("."))
            list.add(parts)
        }
        return list
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        interface ItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

        val itemExamTitle: TextView = view.findViewById(R.id.exam_title)
        val itemExamSubjectTitle: TextView = view.findViewById(R.id.exam_subject_title)
//    val itemImageView: ImageView = view.findViewById(R.id.itemImageView)

        init {
            // layoutの初期設定するときはココ
        }

    }

    class RecyclerAdapter(private val context: Context,
                          private val itemClickListener: RecyclerViewHolder.ItemClickListener,
                          private val itemExamList: ArrayList<ArrayList<String>>)
        : RecyclerView.Adapter<RecyclerViewHolder>() {

        private var mRecyclerView: RecyclerView? = null

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            mRecyclerView = recyclerView
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            mRecyclerView = null

        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.let {
                it.itemExamTitle.text = itemExamList[position][0]
                it.itemExamSubjectTitle.text = itemExamList[position][1]
            }
        }

        override fun getItemCount(): Int {
            return itemExamList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

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
}

