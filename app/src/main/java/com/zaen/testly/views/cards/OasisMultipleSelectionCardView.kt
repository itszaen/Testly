package com.zaen.testly.views.cards

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.MultipleSelectionCardData

class OasisMultipleSelectionCardView(context: Context, override val card: MultipleSelectionCardData, container: FrameLayout) : OasisSelectionBaseCardView(context, card, container) {
    override fun showAnswer() {

    }

    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select ${card.answerList.size} answers."
    }

    fun setUpOptions(listener: OptionClickListener){
        val selectedList = arrayListOf<Int>()
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (card.answerList.contains(index)){
                    optionItemLayout.setOnClickListener(null)
                    // should not and does not add duplicate
                    selectedList.add(index)
                    // effect
                    optionItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))
                    if (selectedList == card.answerList){
                        disableOptions()
                        listener.onRightOptionClick(it)
                    }

                } else {
                    disableOptions()
                    listener.onWrongOptionClick(it)
                }
            }
        }
    }

    override fun setUpPreview() {
        animateAnswer()
    }

    override fun animateAnswer(){
        for (answer in card.answerList){
            optionContainer.getChildAt(answer).setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))

        }
    }
}