package com.zaen.testly.views.cards

import android.content.Context
import android.graphics.drawable.Animatable
import android.support.v4.content.res.ResourcesCompat
import android.widget.FrameLayout
import com.zaen.testly.R
import com.zaen.testly.data.SelectionCardData

class ElixirSelectionCardView(context: Context, override var card: SelectionCardData, container: FrameLayout) : ElixirSelectionBaseCardView(context, card, container),
ISelectionCardView{
    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select one answer."
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

    private fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }
    override fun setUpPreview(){
        showAnswer()
    }

    override fun showAnswer(){
        for ((i, optionItemLayout) in optionItemList.withIndex()){
            val circle = optionItemLayout.findViewById<FrameLayout>(R.id.box_card_selection_option_elixir)
            val correct = ResourcesCompat.getDrawable(context.resources, R.drawable.circle_correct, null)
            if (i == card.answer){
                circle.background = correct
                (correct as Animatable).start()
            } else {

            }
        }
    }

}