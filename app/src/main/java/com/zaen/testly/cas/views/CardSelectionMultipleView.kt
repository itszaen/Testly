package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionMultipleCardData

class CardSelectionMultipleView(context: Context, override val card: SelectionMultipleCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    fun inflate(){
        inflate(card.options)
    }

    fun setUpOptions(listener: OptionClickListener){
        val selectedList = arrayListOf<Int>()
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (card.answerList.contains(index)){
                    optionItemLayout.setOnClickListener(null)
                    // should not and will not add duplicate
                    selectedList.add(index)
                    // effect
                    optionItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))
                    if (selectedList == card.answerList){
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
        for (answer in card.answerList){
            optionContainer.getChildAt(answer).setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))

        }
    }
}