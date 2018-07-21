package com.zaen.testly.views.cards

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import com.zaen.testly.R
import com.zaen.testly.data.MultipleSelectionCardData

class ElixirMultipleSelectionCardView(context: Context, override val card: MultipleSelectionCardData, container: FrameLayout) : ElixirSelectionBaseCardView(context, card, container), IMultipleSelectionCardView {
    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select ${card.answerList.size} answers."
    }

    fun setUpOptions(listener: IMultipleSelectionCardView.MultipleOptionsClickListener){
        val selectedList = arrayListOf<Int>()
        for ((index, optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener {
                // if correct
                if (card.answerList.contains(index)){
                    listener.onCorrectOptionClick(it, index)
                    optionItemLayout.setOnClickListener(null)
                    // onClickListener set to null and does not add duplicate
                    selectedList.add(index)

                    animatePartiallyCorrectAnswer(index)

                    // if all answered correctly
                    if (selectedList.size == card.answerList.size){
                        listener.onAllCorrect()
                        disableOptions()
                        animateAnswer()
                    }
                }
                // if not correct
                else {
                    disableOptions()
                    listener.onIncorrectOptionClick(it,index)
                    animateIncorrectAnswer(index)
                    animateAnswer()
                }
            }
        }
    }

    override fun setUpPreview() {
        for ((i, optionItemLayout) in optionItemList.withIndex()){
            val circle = optionItemLayout.findViewById<ImageView>(R.id.box_card_selection_option_elixir)
            val text = optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option_elixir)

            if (isRightOption(i)){
                showCorrectIcon(i)
                showCorrectText(i)
            }
        }
    }

    override fun isRightOption(index: Int):Boolean{
        return card.answerList.contains(index)
    }

    override fun showAnswer() {
        for (answer in card.answerList){
            showCorrectAnswer(answer)
        }
    }

    override fun animateAnswer(){
        for (answer in card.answerList){
            animateCorrectAnswer(answer)
        }
    }


}