package com.zaen.testly.views.cards

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zaen.testly.R
import com.zaen.testly.data.CardData
import java.util.*

abstract class ElixirSelectionBaseCardView(context: Context, open val card: CardData, protected val container: FrameLayout) : CardView(context), ISelectionBaseCardView {
    val optionItemList = arrayListOf<LinearLayoutCompat>()
    override var hasInflated = false
    private val elixir = inflater.inflate(R.layout.view_card_selection_elixir, container, false)
            as View
    internal val cardObjectiveText: android.support.v7.widget.AppCompatTextView
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    protected val optionContainer: LinearLayout

    init {
        container.addView(elixir)
        cardObjectiveText = elixir.findViewById(R.id.objective_card)
        questionTextView = elixir.findViewById(R.id.text_card_selection_question)
        optionContainer = elixir.findViewById(R.id.option_container_card_selection)
    }

    override fun inflate(options: ArrayList<String>) {
        if (hasInflated) { return }
        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in options.withIndex()){
            // line (separator)
            optionContainer.addView(inflater.inflate(R.layout.separator,optionContainer, false))

            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option_elixir, optionContainer,false)
                as LinearLayoutCompat
            optionItemList.add(optionItemLayout)
            // text
            optionItemLayout.findViewById<AppCompatTextView>(R.id.text_card_selection_option_elixir).text = option
            // add
            optionContainer.addView(optionItemLayout)

            if (index == options.size - 1) {
                // last line
                optionContainer.addView(inflater.inflate(R.layout.separator,optionContainer, false))
            }
        }
    }

    override fun disableOptions() {
        for ((i,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener(null)
            optionItemLayout.isClickable = false
            optionItemLayout.isFocusable = false
        }
    }
}