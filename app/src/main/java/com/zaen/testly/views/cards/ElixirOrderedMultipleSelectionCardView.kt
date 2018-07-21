package com.zaen.testly.views.cards

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.FrameLayout
import com.zaen.testly.data.OrderedMultipleSelectionCardData

class ElixirOrderedMultipleSelectionCardView(context: Context, override val card: OrderedMultipleSelectionCardData, container: FrameLayout): ElixirSelectionBaseCardView(context, card, container){
    fun inflate(){
        inflate(card.options)
        cardObjectiveText.text = "Select ${card.answerList.size} answers in the right order."
    }

    fun setUpOptions(listener: IMultipleSelectionCardView.MultipleOptionsClickListener){
        var answeringCount = 0
        var answeredCount = 0
        for ((index,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener{
                answeredCount += 1
                if (card.answerList[answeringCount] == index){
                    // correct answer
                    optionItemLayout.setOnClickListener(null)
                    listener.onCorrectOptionClick(it, index)

                    // when all correct
                    if (card.answerList.size == answeredCount){
                        disableOptions()
                        listener.onAllCorrect()
                        animateAnswer()
                        return@setOnClickListener
                    }

                    // when one correct
                    @RequiresApi(Build.VERSION_CODES.M)
                    when (answeringCount){
                        0 -> {animatePartiallyCorrectAnswer(index,
                        object: Animatable2.AnimationCallback(){
                            override fun onAnimationEnd(drawable: Drawable?) {
                                super.onAnimationEnd(drawable)
                                showFirstPartiallyCorrectAnswer(index)
                            }
                        })}
                        1 -> {animatePartiallyCorrectAnswer(index,
                                object: Animatable2.AnimationCallback(){
                                    override fun onAnimationEnd(drawable: Drawable?) {
                                        super.onAnimationEnd(drawable)
                                        showSecondPartiallyCorrectAnswer(index)
                                    }
                                })}
                        2 -> {animatePartiallyCorrectAnswer(index,
                                object: Animatable2.AnimationCallback(){
                                    override fun onAnimationEnd(drawable: Drawable?) {
                                        super.onAnimationEnd(drawable)
                                        showThirdPartiallyCorrectAnswer(index)
                                    }
                                })}
                        3 -> {animatePartiallyCorrectAnswer(index,
                                object: Animatable2.AnimationCallback(){
                                    override fun onAnimationEnd(drawable: Drawable?) {
                                        super.onAnimationEnd(drawable)
                                        showFourthPartiallyCorrectAnswer(index)
                                    }
                                })}
                    }


                } else {
                    disableOptions()
                    listener.onIncorrectOptionClick(it, index)
                    if (card.answerList.contains(index)){
                        // is answer but wrong order
                        animateIncorrectAnswers(index)

                    } else {
                        // wrong answer
                        animateIncorrectAnswers(index)
                    }
                }
                answeringCount += 1
            }
        }
    }


    override fun isRightOption(index: Int): Boolean {
        return true
    }

    override fun setUpPreview() {

    }

    override fun showAnswer() {
        for ((i,answer) in card.orderedAnswers.withIndex()){
            when (answer){
                0 -> showFirstCorrectAnswer(i)
                1 -> showSecondCorrectAnswer(i)
                2 -> showThirdCorrectAnswer(i)
                3 -> showFourthCorrectAnswer(i)
            }
        }
    }

    override fun animateAnswer() {
        for ((i,answer) in card.orderedAnswers.withIndex()){
            animateCorrectAnswer(answer)
        }
        showAnswer()
    }

    private fun animateIncorrectAnswers(index: Int){
        animateIncorrectAnswer(index, @TargetApi(Build.VERSION_CODES.M)
        object: Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                showAnswer()
            }
        })

    }

}

