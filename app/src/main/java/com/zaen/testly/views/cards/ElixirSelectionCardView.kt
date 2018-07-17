package com.zaen.testly.views.cards

import android.content.Context
import android.support.v4.content.ContextCompat
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
                    showAnswer()
                    listener.onRightOptionClick(it, index)
                } else {
                    disableOptions()
                    showAnswer()
                    showIncorrectAnswer(index)
                    listener.onWrongOptionClick(it, index)
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
            val circle = optionItemLayout.findViewById<ImageView>(R.id.box_card_selection_option_elixir)
            val text = optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option_elixir)

            if (i == card.answer){
                circle.setImageResource(R.drawable.circle_correct
                        // TODO [A1] animation
                //(circle.drawable as Animatable).start()
                text.setTextColor(ContextCompat.getColor(context, R.color.accent_green))
            }
        }
    }

    fun showIncorrectAnswer(index: Int){
        if (!isRightOption(index)){
            optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir).setImageResource(R.drawable.circle_incorrect)
            optionItemList[index].findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option_elixir).setTextColor(ContextCompat.getColor(context, R.color.accent_red))
        }
    }

}