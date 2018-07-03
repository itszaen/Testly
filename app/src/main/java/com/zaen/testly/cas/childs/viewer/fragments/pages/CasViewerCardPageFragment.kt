package com.zaen.testly.cas.childs.viewer.fragments.pages

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.cas.views.CardSelectionMultipleOrderedView
import com.zaen.testly.cas.views.CardSelectionMultipleView
import com.zaen.testly.cas.views.CardSelectionView
import com.zaen.testly.data.*
import kotlinx.android.synthetic.main.fragment_page_cas_viewer_card.*
import org.parceler.Parcels

class CasViewerCardPageFragment : BaseFragment(){
    private var card: CardData? = null

    companion object {
        fun newInstance(card: CardData): CasViewerCardPageFragment {
            val frag = CasViewerCardPageFragment()
            val bundle = Bundle()
            bundle.putParcelable(FirebaseDocument.CARD,Parcels.wrap(card))
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes = R.layout.fragment_page_cas_viewer_card
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            card = Parcels.unwrap(arguments?.getParcelable<Parcelable>(FirebaseDocument.CARD))
            when (card?.cardType){
                CardData.CARD_TYPE_SELECTION -> {
                    val cardView = CardSelectionView(activity!!,card as SelectionCardData,view_container_fragment_page_cas_viewer_card)
                    cardView.inflate()
                    //cardView.showAnswer()
                }
                CardData.CARD_TYPE_SELECTION_MULTIPLE -> {
                    val cardView = CardSelectionMultipleView(activity!!, card as SelectionMultipleCardData, view_container_fragment_page_cas_viewer_card)
                    cardView.inflate()
                    //cardView.showAnswers()
                }
                CardData.CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                    val cardView = CardSelectionMultipleOrderedView(activity!!, card as SelectionMultipleOrderedCardData, view_container_fragment_page_cas_viewer_card)
                    cardView.inflate()
                    //cardView.showAnswers()
                }
                CardData.CARD_TYPE_SPELLING -> {
                }

            }
        }
    }
}