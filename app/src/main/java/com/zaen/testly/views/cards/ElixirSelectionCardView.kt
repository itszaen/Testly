package com.zaen.testly.views.cards

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
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
                    showFirstCorrectAnswer(index)
                    listener.onCorrectOptionClick(it, index)
                } else {
                    disableOptions()
                    animateAnswer()
                    animateIncorrectAnswer(index)
                    listener.onIncorrectOptionClick(it, index)
                }
            }
        }
    }

    override fun isRightOption(index: Int):Boolean{
        return index == card.answer
    }

    override fun setUpPreview(){
        for ((i, optionItemLayout) in optionItemList.withIndex()){
            val circle = optionItemLayout.findViewById<ImageView>(R.id.box_card_selection_option_elixir)
            val text = optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option_elixir)

            if (i == card.answer){
                showCorrectIcon(circle)
                showCorrectText(text)
            }
        }
    }

    override fun showAnswer() {
        showCorrectAnswer(card.answer)
    }

    override fun animateAnswer(){
        animateCorrectAnswer(card.answer)
    }



}