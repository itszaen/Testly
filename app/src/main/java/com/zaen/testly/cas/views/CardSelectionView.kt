package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class CardSelectionView(context: Context, override var card: SelectionCardData, container: FrameLayout) : CardSelectionBaseView(context, card, container) {
    fun inflate(){
        inflate(card.options)
    }

    private fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }

    fun setUpOptions(listener: OptionClickListener){
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                if (isRightOption(index)){
                    disableOption()
                    listener.onRightOptionClick(it)
                } else {
                    disableOption()
                    listener.onWrongOptionClick(it)
                }
            }
        }
    }

    override fun setUpPreview(){
        showAnswer()
    }

    override fun showAnswer(){
        optionContainer.getChildAt(card.answer).setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue))
    }



}