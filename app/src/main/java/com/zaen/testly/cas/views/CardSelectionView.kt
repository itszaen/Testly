package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class CardSelectionView(context: Context, override var card: SelectionCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    private val optionItemList = arrayListOf<mehdi.sakout.fancybuttons.FancyButton>()
    override fun inflate(){
        if (hasInflated){ return }
        hasInflated = true
        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in card.options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer,false)
                    as mehdi.sakout.fancybuttons.FancyButton
            optionItemList.add(optionItemLayout)
            optionItemLayout.setText(option)
            optionContainer.addView(optionItemLayout)
        }
    }

    private fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }

    fun setUpOptions(listener: OptionClickListener){
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (isRightOption(index)){ listener.onRightOptionClick(it) } else { listener.onWrongOptionClick(it) }
            }
        }
    }

    override fun showAnswer(){
        optionContainer.getChildAt(card.answer).setBackgroundColor(ContextCompat.getColor(context, R.color.accent_black))
    }



}