package com.zaen.testly.cas.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class CardSelectionView(context: Context, override var card: SelectionCardData, container: FrameLayout,
                        private val listener: OptionClickListener) : CardSelectionBaseView(context, card, container) {
    interface OptionClickListener{
        fun onRightOptionClick(view: View)
        fun onWrongOptionClick(view: View)
    }
    override fun inflate(){
        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in card.options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer,false)
                    as android.support.v7.widget.CardView
            optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option).text = option
            optionContainer.addView(optionItemLayout)
            optionItemLayout.setOnClickListener{
                if (isRightOption(index)){ listener.onRightOptionClick(it) } else { listener.onWrongOptionClick(it) }
            }
        }
    }

    private fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }

    override fun showAnswer(){
        optionContainer.getChildAt(card.answer).setBackgroundResource(R.color.md_light_blue_100)
    }



}