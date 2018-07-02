package com.zaen.testly.cas.views

import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.data.SelectionMultipleCardData

class CardSelectionMultipleView(context: BaseActivity, val card: SelectionMultipleCardData, private val container: FrameLayout) : CardView(context) {
    private val view = inflater.inflate(R.layout.view_card_selection, container)
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    private val optionContainer: LinearLayout
    init {
//        container.addView(view)
        questionTextView = view.findViewById(R.id.text_card_selection_question)
        optionContainer  = view.findViewById(R.id.option_container_card_selection)
    }
    fun inflate(){
        // Question
        questionTextView.text = card.question

        // Option
        for ((index, option) in card.options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer)
                    //as android.support.v7.widget.CardView
            optionItemLayout.findViewById<android.support.v7.widget.AppCompatTextView>(R.id.text_card_selection_option).text = option
            val optionHeight = 32
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, optionHeight)
            optionItemLayout.layoutParams = params
        }
    }
    fun showAnswers(){
        for (answer in card.answerList){
            optionContainer.getChildAt(answer).setBackgroundColor(ContextCompat.getColor(context, R.color.md_light_blue_100))

        }
    }
}