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

                    // when all correct
                    if (card.answerList.size == answeringCount){
                        disableOptions()
                        listener.onAllCorrect()
                        animateAnswer()
                        return@setOnClickListener
                    }

                    // when one correct
                    when (answeringCount){
                        1 -> {animatePartiallyCorrectAnswer(index);showFirstPartiallyCorrectAnswer(index)}
                        2 -> {animatePartiallyCorrectAnswer(index);showSecondPartiallyCorrectAnswer(index)}
                        3 -> {animatePartiallyCorrectAnswer(index);showThirdPartiallyCorrectAnswer(index)}
                        4 -> {animatePartiallyCorrectAnswer(index);showFourthPartiallyCorrectAnswer(index)}
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
        for (i in card.orderedAnswers){
            when (i){
                1 -> showFirstCorrectAnswer(i)
                2 -> showSecondCorrectAnswer(i)
                3 -> showThirdCorrectAnswer(i)
                4 -> showFourthCorrectAnswer(i)
            }
        }
    }

    override fun animateAnswer() {
        for (i in card.orderedAnswers){
            animateCorrectAnswer(i)
        }
        showAnswer()
    }


}

