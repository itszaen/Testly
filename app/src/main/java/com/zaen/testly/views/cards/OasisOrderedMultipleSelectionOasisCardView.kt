package com.zaen.testly.views.cards

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.OrderedMultipleSelectionCardData

class OasisOrderedMultipleSelectionOasisCardView(context: Context, override val card: OrderedMultipleSelectionCardData, container: FrameLayout) : OasisSelectionBaseCardView(context, card, container) {
    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select ${card.answerList.size} answers in the right order."
    }

    fun setUpOptions(listener: OptionClickListener){
        var answeringCount = 0
        val selectedList = arrayListOf<Int>()
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (card.answerList[answeringCount] == index){
                    optionItemLayout.setOnClickListener(null)
                    // effect
                    optionItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))

                    answeringCount += 1
                    // if over
                    if (card.answerList.size == answeringCount){
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

    override fun showAnswer() {

    }

    override fun animateAnswer(){


    }
}