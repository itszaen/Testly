package com.zaen.testly.cas.childs.create_card.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zaen.testly.R
import com.zaen.testly.R.id.*
import com.zaen.testly.TestlyFirestore
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.cas.views.CardSelectionMultipleOrderedView
import com.zaen.testly.cas.views.CardSelectionMultipleView
import com.zaen.testly.cas.views.CardSelectionView
import com.zaen.testly.data.*
import com.zaen.testly.data.FirebaseDocument.Companion.CARD
import kotlinx.android.synthetic.main.fragment_create_card_preview.*
import kotlinx.android.synthetic.main.view_card_selection.*
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
        when (card){
            is SelectionCardData -> {
                val view = CardSelectionView(activity!!,card as SelectionCardData, view_container_create_card_preview)
                view.inflate()
                view.showAnswer()
            }
            is SelectionMultipleCardData -> {
                val view = CardSelectionMultipleView(activity!!, card as SelectionMultipleCardData, view_container_create_card_preview)
                view.inflate()
                view.showAnswer()
            }
            is SelectionMultipleOrderedCardData -> {
                val view = CardSelectionMultipleOrderedView(activity!!, card as SelectionMultipleOrderedCardData, view_container_create_card_preview)
                view.inflate()
                view.showAnswer()
            }
            is SpellingCardData -> {

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