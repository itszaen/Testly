package com.zaen.testly.fragments.cas

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaen.testly.R
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.data.*
import com.zaen.testly.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_card_preview.*
import kotlinx.android.synthetic.main.view_card_selection.*
import org.parceler.Parcels

class PreviewCardFragment : BaseFragment() {
    companion object {
        const val CARD = "card"
    }

    interface SubmitCardSuccess{
        fun onSubmitSuccessful()
    }
    private var mListener: SubmitCardSuccess? = null

    var card: CardData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Parcels.unwrap(arguments?.getParcelable<Parcelable>(CARD))
        retainInstance = true
        if (savedInstanceState != null){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SubmitCardSuccess){
            mListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_create_card_preview
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var view: View? = null
        if (card != null){
            when (card){
                is SelectionCardData -> {
                    view = layoutInflater.inflate(R.layout.view_card_selection,null)
                }
                is SelectionMultipleCardData -> {
                    view = layoutInflater.inflate(R.layout.view_card_selection,null)
                }
                is SelectionMultipleOrderedCardData -> {
                    view = layoutInflater.inflate(R.layout.view_card_selection,null)
                }
                is SpellingCardData -> {
                    view = layoutInflater.inflate(R.layout.view_card_spelling,null)
                }
            }
            if (view != null){
                view_container_create_card_preview.addView(view)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            return
        }
        if (card != null){

            var optionCount: Int? = null
            var options: ArrayList<String>?
            var answer: Int?
            var answers: ArrayList<Int>

            when (card){
                is SelectionCardData -> {
                    card = (card as SelectionCardData)
                    text_card_selection_question.text = (card as SelectionCardData).question
                    options = (card as SelectionCardData).options
                    answer = (card as SelectionCardData).answer
                    optionCount = options.size
                    for ((i,v) in (card as SelectionCardData).options.withIndex()){
                        val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option,null)
                        ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1F)
                        optionLayout.layoutParams = params
                        if (i == answer){
                            optionLayout.setBackgroundColor(ContextCompat.getColor(context!!,R.color.md_light_blue_100))
                        }
                        option_container_card_selection.addView(optionLayout)
                    }
                }
                is SelectionMultipleCardData -> {
                    card = (card as SelectionMultipleCardData)
                    text_card_selection_question.text = (card as SelectionMultipleCardData).question
                    options = (card as SelectionMultipleCardData).options
                    optionCount = options.size
                    answers = (card as SelectionMultipleCardData).answerList
                    for ((i,v) in (card as SelectionMultipleCardData).options.withIndex()){
                        val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option,null)
                        ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1F)
                        optionLayout.layoutParams = params
                        if (answers.contains(i)){
                            optionLayout.setBackgroundColor(ContextCompat.getColor(context!!,R.color.md_light_blue_100))
                        }
                        option_container_card_selection.addView(optionLayout)
                    }
                }
                is SelectionMultipleOrderedCardData -> {
                    card = (card as SelectionMultipleOrderedCardData)
                    text_card_selection_question.text = (card as SelectionMultipleOrderedCardData).question
                    options = (card as SelectionMultipleOrderedCardData).options
                    optionCount = options.size
                    for ((i,v) in (card as SelectionMultipleOrderedCardData).options.withIndex()){
                        val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option,null)
                        ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1F)
                        optionLayout.layoutParams = params
                        option_container_card_selection.addView(optionLayout)
                    }
                }
                is SpellingCardData -> {

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    @OnClick(R.id.btn_preview_cancel)
    fun onCancel(){
        onBackPressedSupport()
    }

    @OnClick(R.id.btn_preview_submit)
    fun onSubmit(){
        card!!.timestamp = System.currentTimeMillis() / 1000L
        val path = FirebaseFirestore.getInstance().collection("cards")
        TestlyFirestore(this).addDocumentToCollection(path,card!!,object: TestlyFirestore.UploadToCollectionListener{
            override fun onDocumentUpload(path: Query, reference: DocumentReference?, exception: Exception?) {
                if (reference != null){
                    mListener?.onSubmitSuccessful()
                }
            }
        })
    }
}