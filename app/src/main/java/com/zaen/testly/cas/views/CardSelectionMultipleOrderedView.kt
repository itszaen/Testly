package com.zaen.testly.cas.views

import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.data.SelectionMultipleOrderedCardData

class CardSelectionMultipleOrderedView(context: BaseActivity, val card: SelectionMultipleOrderedCardData, val container: FrameLayout) : CardView(context) {
    private val view = inflater.inflate(R.layout.view_card_selection, container)
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    private val optionContainer: LinearLayout

    init {
//        container.addView(view)
        questionTextView = view.findViewById(R.id.text_card_selection_question)
        optionContainer = view.findViewById(R.id.option_container_card_selection)
    }
    fun inflate(){

        // Question
        questionTextView.text = card.question

        // Options
        for ((i, v) in card.options.withIndex()) {
            val optionLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
            ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            optionLayout.layoutParams = params
            optionContainer.addView(optionLayout)
        }
    }

    fun showAnswers(){

    }
}