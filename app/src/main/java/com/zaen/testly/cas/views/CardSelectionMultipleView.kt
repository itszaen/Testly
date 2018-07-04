package com.zaen.testly.cas.views

import android.content.Context
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionMultipleCardData

class CardSelectionMultipleView(context: Context, override val card: SelectionMultipleCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    override fun inflate(){
        // Question
        questionTextView.text = card.question

        // Option
        for ((index, option) in card.options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer, false)
                    as android.support.v7.widget.CardView
            optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option).text = option
            optionContainer.addView(optionItemLayout)
        }
    }
    override fun showAnswer(){
        for (answer in card.answerList){
            optionContainer.getChildAt(answer).setBackgroundResource(R.color.md_light_blue_100)

        }
    }
}