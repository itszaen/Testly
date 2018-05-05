package com.zaen.testly.views.recyclers

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.baoyz.widget.PullRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.zaen.testly.R
import es.dmoral.toasty.Toasty
import java.util.*

/**
 * Created by zaen on 3/29/18.
 */
class FeedbackView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr)
{
    companion object {
        const val TAG = "FeedbackView"
    }

    var refreshLayout: PullRefreshLayout? = null

    init {
        getFeedbackDocumentData()
        val inflater = LayoutInflater.from(getContext())
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        inflater.inflate(R.layout.view_feedback,null)
        this.overScrollMode = View.OVER_SCROLL_NEVER
        refreshLayout = findViewById(R.id.view_feedback)
        refreshLayout?.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES)
        refreshLayout?.setOnRefreshListener {
            getFeedbackDocumentData()
        }
    }

    private fun getFeedbackDocumentData(): ArrayList<Map<String, Any>> {
        val list = ArrayList<Map<String, Any>>()
        FirebaseFirestore.getInstance().collection("feedback").get()
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        Log.d(TAG, "Getting feedback documents.")
                        for (document: QueryDocumentSnapshot in it.result){
                            Log.d(TAG, document.id + "->" + document.data)
                            list.add(document.data)
                        }
                        this.adapter = RecyclerAdapter(context, object: RecyclerViewHolder.ItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                Toasty.info(context,"feedback item $position was tapped", Toast.LENGTH_SHORT,true).show()
                            }
                        },
                                list)
                        refreshLayout?.setRefreshing(false)
                    } else {
                        Log.w(TAG, "Getting feedback document Failure. Exception: ${it.exception}")
                    }
                }
        return list
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        interface ItemClickListener {
            fun onItemClick(view: View, position: Int)
        }

        val upvoteCount: TextView = view.findViewById(R.id.count_upvote)
        val titleText: TextView = view.findViewById(R.id.title_item_feedback)
        val tagText: TextView = view.findViewById(R.id.tag_item_feedback)
        val captionText: TextView = view.findViewById(R.id.caption_item_feedback)
        val commentCount: TextView = view.findViewById(R.id.count_comment)

        init {
            // initialize layout
        }

    }

    class RecyclerAdapter(private val context: Context,
                          private val itemClickListener: RecyclerViewHolder.ItemClickListener,
                          private val feedbackDocumentList: ArrayList<Map<String,Any>>)
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
            val document = feedbackDocumentList[position]
            val upvoteCount = document["upvote_count"].toString()
            val title = document["title"].toString()
            val tag = document["tag"]
            val captionString = document["content"].toString()
            val captionStringAbv = captionString.substring(0,Math.min(captionString.length,30)) + "..."
            holder.let {
                it.upvoteCount.text = upvoteCount
                it.titleText.text = title
                when (tag){
                    "PLANNED" -> {
                        it.tagText.visibility = View.VISIBLE
                        it.tagText.text = context.resources.getString(R.string.tag_planned)
                    }
                }
                it.captionText.text = captionStringAbv
                if (document["comment_count"] as Long > 0){
                    it.commentCount.visibility = View.VISIBLE
                    it.commentCount.text = document["comment_count"].toString()
                }
            }
        }

        override fun getItemCount(): Int {
            return feedbackDocumentList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

            val layoutInflater = LayoutInflater.from(context)
            val mView = layoutInflater.inflate(R.layout.item_feedback, parent, false)

            mView.setOnClickListener { view ->
                mRecyclerView?.let {
                    itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
                }
            }
            return RecyclerViewHolder(mView)
        }

    }
}
