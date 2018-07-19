package com.zaen.testly.views.cards

import android.content.Context
import android.graphics.drawable.Animatable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
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
            if (index == 0) {
                // first line
                inflateLongSeparator()
            } else {
                // line
                inflateShortSeparator()
            }

            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option_elixir, optionContainer,false)
                as LinearLayoutCompat
            optionItemList.add(optionItemLayout)
            // text
            optionItemLayout.findViewById<AppCompatTextView>(R.id.text_card_selection_option_elixir).text = option
            // add
            optionContainer.addView(optionItemLayout)

            // last line
            if (index == options.size - 1) {
                inflateLongSeparator()
            }
        }
    }

    private fun inflateLongSeparator(){
        optionContainer.addView(inflater.inflate(R.layout.separator_long,optionContainer, false))
    }

    private fun inflateShortSeparator(){
        optionContainer.addView(inflater.inflate(R.layout.separator_short,optionContainer, false))
    }

    protected fun disableOptions() {
        for ((i,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener(null)
            optionItemLayout.isClickable = false
            optionItemLayout.isFocusable = false
        }
    }

    abstract fun isRightOption(index :Int): Boolean

    // show answer stuff
    protected fun showCorrectAnswer(index: Int){
        showCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showCorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun animateCorrectAnswer(index: Int){
        animateCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showCorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun showIncorrectAnswer(index: Int){
        showIncorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showIncorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun animateIncorrectAnswer(index: Int){
        animateIncorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showIncorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun showCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_correct)
    }

    private fun animateCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_correct_animated_check)
        (imageView.drawable as Animatable).start()
    }

    protected fun showCorrectText(text: AppCompatTextView){
        text.setTextColor(ContextCompat.getColor(context, R.color.card_correct_color))
    }

    protected fun showIncorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_incorrect)
    }

    private fun animateIncorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_incorrect_animated_cross)
        (imageView.drawable as Animatable).start()
    }

    private fun showIncorrectText(text: AppCompatTextView){
        text.setTextColor(ContextCompat.getColor(context, R.color.card_incorrect_color))
    }

    /// multiple
    protected fun showPartiallyCorrectAnswer(index: Int){
        showPartiallyCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showPartiallyCorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun animatePartiallyCorrectAnswer(index: Int){
        animatePartiallyCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showPartiallyCorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    protected fun showPartiallyCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_partially_correct)
    }

    private fun animatePartiallyCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_partially_correct_animated_check)
        (imageView.drawable as Animatable).start()
    }

    private fun showPartiallyCorrectText(text: AppCompatTextView){
        text.setTextColor(ContextCompat.getColor(context, R.color.card_partially_correct_color))
    }

    /// ordered
    protected fun showFirstCorrectAnswer(index: Int){
        showFirstCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showPartiallyCorrectText(optionItemList[index].findViewById(R.id.text_card_selection_option_elixir))
    }

    private fun showFirstCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_correct_first)
    }

}