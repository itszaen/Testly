package com.zaen.testly.cas.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.CardData

abstract class CardSelectionBaseView(context: Context, open val card: CardData, protected val container: FrameLayout) : CardView(context) {
    private val cardSelectionView= inflater.inflate(R.layout.view_card_selection,container,false) as android.support.v7.widget.CardView
    protected val questionTextView: android.support.v7.widget.AppCompatTextView
    protected val optionContainer: LinearLayout
    init {
        container.addView(cardSelectionView)
        questionTextView = cardSelectionView.findViewById(R.id.text_card_selection_question)
        optionContainer = cardSelectionView.findViewById(R.id.option_container_card_selection)
    }

    abstract fun inflate()
    abstract fun showAnswer()

}