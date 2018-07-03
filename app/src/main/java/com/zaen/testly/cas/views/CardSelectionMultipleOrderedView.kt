package com.zaen.testly.cas.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionMultipleOrderedCardData

class CardSelectionMultipleOrderedView(context: Context, override val card: SelectionMultipleOrderedCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    override fun inflate(){
        // Question
        questionTextView.text = card.question

        // Options
        for ((i, v) in card.options.withIndex()) {
            val optionLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
            ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            optionLayout.layoutParams = params
            optionContainer.addView(optionLayout)
        }
    }

    override fun showAnswer(){

    }
}