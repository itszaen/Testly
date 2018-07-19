package com.zaen.testly.views.cards

import android.content.Context
import android.widget.FrameLayout
import com.zaen.testly.data.OrderedMultipleSelectionCardData

class ElixirOrderedMultipleSelectionCardView(context: Context, override val card: OrderedMultipleSelectionCardData, container: FrameLayout): ElixirSelectionBaseCardView(context, card, container){
    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select ${card.answerList.size} answers in the right order."
    }

    fun setUpOptions(listener: IMultipleSelectionCardView.MultipleOptionsClickListener){
        var answeringCount = 0
        val selectedList = arrayListOf<Int>()
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                answeringCount += 1
                if (card.answerList[answeringCount] == index){
                    // correct answer
                    optionItemLayout.setOnClickListener(null)
                    listener.onCorrectOptionClick(it, index)

                    // effect
                    showFirstCorrectAnswer(index)

                    // when all correct
                    if (card.answerList.size == answeringCount){
                        disableOptions()
                        listener.onAllCorrect()
                        animateAnswer()
                    }

                } else {
                    disableOptions()
                    listener.onIncorrectOptionClick(it, index)
                    if (card.answerList.contains(index)){
                        // is answer but wrong order
                        animateAnswer()

                    } else {
                        // wrong answer
                        animateIncorrectAnswer(index)
                    }
                }
            }
        }
    }


    override fun isRightOption(index: Int): Boolean {
        return true
    }

    override fun setUpPreview() {

    }

    override fun showAnswer() {

    }

    override fun animateAnswer() {

    }


}

