package com.zaen.testly.cas.children.createCard.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaen.testly.R
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.views.cards.OasisOrderedMultipleSelectionOasisCardView
import com.zaen.testly.views.cards.OasisMultipleSelectionCardView
import com.zaen.testly.views.cards.OasisSelectionCardView
import com.zaen.testly.data.*
import com.zaen.testly.data.FirebaseDocument.Companion.CARD
import kotlinx.android.synthetic.main.fragment_create_card_preview.*
import org.parceler.Parcels

class PreviewCardFragment : BaseFragment() {
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null){
            return
        }
        initializeViews()
        when (card){
            is SelectionCardData -> {
                val view = OasisSelectionCardView(activity!!, card as SelectionCardData, view_container_create_card_preview)
                view.inflate()
                view.animateAnswer()
            }
            is MultipleSelectionCardData -> {
                val view = OasisMultipleSelectionCardView(activity!!, card as MultipleSelectionCardData, view_container_create_card_preview)
                view.inflate()
                view.animateAnswer()
            }
            is OrderedMultipleSelectionCardData -> {
                val view = OasisOrderedMultipleSelectionOasisCardView(activity!!, card as OrderedMultipleSelectionCardData, view_container_create_card_preview)
                view.inflate()
                view.animateAnswer()
            }
            is SpellingCardData -> {

            }
        }
    }

    private fun initializeViews(){
        btn_preview_cancel.setOnClickListener{ onBackPressedSupport() }
        btn_preview_submit.setOnClickListener { onSubmit() }
    }

    fun onSubmit(){
        // Progress
        val dialog = MaterialDialog.Builder(activity!!)
                .title("Uploading...")
                .content("")
                .progress(false,1,true)
                .canceledOnTouchOutside(false)
                .show()

        val path = FirebaseFirestore.getInstance().collection("cards").document()
        card!!.id = path.id
        card!!.timestamp = System.currentTimeMillis() / 1000L

        TestlyFirestore(this).addDocumentToCollection(path,card!!,object: TestlyFirestore.UploadToCollectionListener{
            override fun onDocumentUpload(path: Query, reference: DocumentReference?, exception: Exception?) {
                if (reference != null){
                    while(dialog.currentProgress < dialog.maxProgress){
                        if (dialog.isCancelled){ break }
                        dialog.incrementProgress(1)
                    }
                    if (dialog.currentProgress >= dialog.maxProgress){
                        mListener?.onSubmitSuccessful()
                    }
                }
            }
        })
    }
}