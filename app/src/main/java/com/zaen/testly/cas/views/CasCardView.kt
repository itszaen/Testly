package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.CardData
import com.zaen.testly.data.SelectionCardData
import com.zaen.testly.data.SelectionMultipleCardData
import com.zaen.testly.data.SelectionMultipleOrderedCardData

class CasCardView(val context: Context, var card: CardData){
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    fun inflateCardViewLayout(container: FrameLayout){
        when (card.cardType){
            CardData.CARD_TYPE_SELECTION -> {
                container.addView(layoutInflater.inflate(R.layout.view_card_selection,null))
            }
            CardData.CARD_TYPE_SELECTION_MULTIPLE -> {
                container.addView(layoutInflater.inflate(R.layout.view_card_selection,null))
            }
            CardData.CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                container.addView(layoutInflater.inflate(R.layout.view_card_selection,null))
            }
            CardData.CARD_TYPE_SPELLING -> {
                container.addView(layoutInflater.inflate(R.layout.view_card_spelling,null))
            }
        }
    }

    fun inflateCardView(
            questionTextView: android.support.v7.widget.AppCompatTextView,
            optionContainer: LinearLayout
    ){
        var view: View? = null
        var optionCount: Int? = null
        val options: ArrayList<String>?
        val answer: Int?
        val answers: ArrayList<Int>
        when (card.cardType){
            CardData.CARD_TYPE_SELECTION -> {
                card = (card as SelectionCardData)
                questionTextView.text = card.question
                options = (card as SelectionCardData).options
                answer = (card as SelectionCardData).answer
                optionCount = options.size
                for ((i, v) in (card as SelectionCardData).options.withIndex()) {
                    val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
                    ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
                    optionLayout.layoutParams = params
                    if (i == answer) {
                        optionLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_light_blue_100))
                    }
                    optionContainer.addView(optionLayout)
                }
            }
            CardData.CARD_TYPE_SELECTION_MULTIPLE -> {
                card = (card as SelectionMultipleCardData)
                questionTextView.text = (card as SelectionMultipleCardData).question
                options = (card as SelectionMultipleCardData).options
                optionCount = options.size
                answers = (card as SelectionMultipleCardData).answerList
                for ((i, v) in (card as SelectionMultipleCardData).options.withIndex()) {
                    val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
                    ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
                    optionLayout.layoutParams = params
                    if (answers.contains(i)) {
                        optionLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.md_light_blue_100))
                    }
                    optionContainer.addView(optionLayout)
                }
            }
            CardData.CARD_TYPE_SELECTION_MULTIPLE_ORDERED -> {
                card = (card as SelectionMultipleOrderedCardData)
                questionTextView.text = (card as SelectionMultipleOrderedCardData).question
                options = (card as SelectionMultipleOrderedCardData).options
                optionCount = options.size
                for ((i, v) in (card as SelectionMultipleOrderedCardData).options.withIndex()) {
                    val optionLayout = layoutInflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
                    ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
                    optionLayout.layoutParams = params
                    optionContainer.addView(optionLayout)
                }
            }
            CardData.CARD_TYPE_SPELLING -> {
            }
        }
    }
}