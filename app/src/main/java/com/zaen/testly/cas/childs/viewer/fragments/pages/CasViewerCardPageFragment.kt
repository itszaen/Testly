package com.zaen.testly.cas.childs.viewer.fragments.pages

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaen.testly.R
import com.zaen.testly.cas.views.CardView
import com.zaen.testly.data.CardData
import com.zaen.testly.data.FirebaseDocument
import com.zaen.testly.base.fragments.BaseFragment
import org.parceler.Parcels
import kotlinx.android.synthetic.main.fragment_page_cas_viewer_card.*
import kotlinx.android.synthetic.main.view_card_selection.*

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
            val cardView = CardView(activity!!,card!!)
            cardView.inflateCardViewLayout(view_container_fragment_page_cas_viewer_card)
            cardView.inflateCardView(
                    text_card_selection_question,
                    option_container_card_selection

            )
        }
    }
}