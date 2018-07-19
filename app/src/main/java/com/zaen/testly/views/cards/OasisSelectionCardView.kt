package com.zaen.testly.views.cards

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class OasisSelectionCardView(context: Context, override var card: SelectionCardData, container: FrameLayout) : OasisSelectionBaseCardView(context, card, container), ISelectionCardView {
    override fun showAnswer() {

    }

    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select one answer."
    }

    private fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }

    fun setUpOptions(listener: OptionClickListener){
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (isRightOption(index)){
                    disableOptions()
                    listener.onRightOptionClick(it)
                } else {
                    disableOptions()
                    listener.onWrongOptionClick(it)
                }
            }
        }
    }

    override fun setUpPreview(){
        animateAnswer()
    }

    override fun animateAnswer(){
        for ((i, optionItemLayout) in optionItemList.withIndex()){
            if (i == card.answer){
                optionItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_green))
            } else {
                optionItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_red))
            }
        }
    }



}