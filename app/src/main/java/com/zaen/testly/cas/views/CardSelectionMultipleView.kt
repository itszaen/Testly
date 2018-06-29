package com.zaen.testly.cas.views

import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.data.SelectionMultipleCardData

class CardSelectionMultipleView(context: BaseActivity, val card: SelectionMultipleCardData, private val container: FrameLayout) : CardView(context) {
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    private val optionContainer: LinearLayout
    init {
        container.addView(inflater.inflate(R.layout.view_card_selection,null))
        questionTextView = context.findViewById(R.id.text_card_selection_question)
        optionContainer  = context.findViewById(R.id.option_container_card_selection)
    }
    fun inflate(){
        // Question
        questionTextView.text = card.question
        for ((i, v) in card.options.withIndex()) {
            val optionLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
            ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            optionLayout.layoutParams = params
            if (card.answerList.contains(i)) {
                optionLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_light_blue_100))
            }
            optionContainer.addView(optionLayout)
        }
    }
    fun showAnswers(){
        for (answer in card.answerList){
            optionContainer.getChildAt(answer).setBackgroundColor(ContextCompat.getColor(context, R.color.md_light_blue_100))

        }
    }
}