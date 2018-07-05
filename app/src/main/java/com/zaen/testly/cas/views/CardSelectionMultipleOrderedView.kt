package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionMultipleOrderedCardData

class CardSelectionMultipleOrderedView(context: Context, override val card: SelectionMultipleOrderedCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    fun inflate(){
        inflate(card.options)
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
                        disableOption()
                        listener.onRightOptionClick(it)
                    }

                } else {
                    disableOption()
                    listener.onWrongOptionClick(it)
                }
            }
        }
    }

    override fun setUpPreview() {
        showAnswer()
    }

    override fun showAnswer(){


    }
}