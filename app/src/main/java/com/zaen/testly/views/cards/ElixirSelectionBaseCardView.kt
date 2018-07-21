package com.zaen.testly.views.cards

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.os.Build
import android.support.annotation.RequiresApi
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
        showCorrectIcon(index)
        showCorrectText(index)
    }

    protected fun animateCorrectAnswer(index: Int){
        animateCorrectIcon(index)
        showCorrectText(index)
    }

    protected fun showIncorrectAnswer(index: Int){
        showIncorrectIcon(index)
        showIncorrectText(index)
    }

    protected fun animateIncorrectAnswer(index: Int){
        animateIncorrectIcon(index)
        showIncorrectText(index)
    }

    protected fun animateIncorrectAnswer(index: Int, listener: Animatable2.AnimationCallback){
        animateIncorrectIcon(index, listener)
        showIncorrectText(index)
    }

    protected fun showCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct)
    }

    private fun animateCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_animated_check)
        (imageView.drawable as Animatable).start()
    }

    protected fun showCorrectText(index: Int){
        val text = optionItemList[index].findViewById<AppCompatTextView>(R.id.text_card_selection_option_elixir)
        text.setTextColor(ContextCompat.getColor(context, R.color.card_correct_color))
    }

    protected fun showIncorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_incorrect)
    }

    private fun animateIncorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_incorrect_animated_cross)
        (imageView.drawable as Animatable).start()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun animateIncorrectIcon(index: Int, listener: Animatable2.AnimationCallback){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_incorrect_animated_cross)
        val animation = imageView.drawable as Animatable2
        animation.registerAnimationCallback(listener)
        animation.start()
    }

    private fun showIncorrectText(index: Int){
        val text = optionItemList[index].findViewById<AppCompatTextView>(R.id.text_card_selection_option_elixir)
        text.setTextColor(ContextCompat.getColor(context, R.color.card_incorrect_color))
    }

    /// multiple
    protected fun showPartiallyCorrectAnswer(index: Int){
        showPartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    protected fun animatePartiallyCorrectAnswer(index: Int){
        animatePartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    protected fun animatePartiallyCorrectAnswer(index: Int, listener: Animatable2.AnimationCallback){
        animatePartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    protected fun showPartiallyCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_partially_correct)
    }

    private fun animatePartiallyCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_partially_correct_animated_check)
        (imageView.drawable as Animatable).start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animatePartiallyCorrectIcon(index: Int, listener: Animatable2.AnimationCallback){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_partially_correct_animated_check)
        val animation = imageView.drawable as Animatable2
        animation.start()
    }

    private fun showPartiallyCorrectText(index: Int){
        val text = optionItemList[index].findViewById<AppCompatTextView>(R.id.text_card_selection_option_elixir)
        text.setTextColor(ContextCompat.getColor(context, R.color.card_partially_correct_color))
    }

    /// ordered
    protected fun showFirstPartiallyCorrectAnswer(index: Int){
        showFirstPartiallyCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showPartiallyCorrectText(index)
    }

    private fun showFirstPartiallyCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_correct_partially_1)
    }

    protected fun showSecondPartiallyCorrectAnswer(index: Int){
        showSecondPartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    private fun showSecondPartiallyCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_partially_2)
    }

    protected fun showThirdPartiallyCorrectAnswer(index: Int){
        showThirdPartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    private fun showThirdPartiallyCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_partially_3)
    }

    protected fun showFourthPartiallyCorrectAnswer(index: Int){
        showFourthPartiallyCorrectIcon(index)
        showPartiallyCorrectText(index)
    }

    private fun showFourthPartiallyCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_partially_4)
    }

    protected fun showFirstCorrectAnswer(index: Int){
        showFirstCorrectIcon(optionItemList[index].findViewById(R.id.box_card_selection_option_elixir))
        showCorrectText(index)
    }

    private fun showFirstCorrectIcon(imageView: ImageView){
        imageView.setImageResource(R.drawable.circle_correct_1)
    }

    protected fun showSecondCorrectAnswer(index: Int){
        showSecondCorrectIcon(index)
        showCorrectText(index)
    }

    private fun showSecondCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_2)
    }

    protected fun showThirdCorrectAnswer(index: Int){
        showThirdCorrectIcon(index)
        showCorrectText(index)
    }

    private fun showThirdCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_3)
    }

    protected fun showFourthCorrectAnswer(index: Int){
        showFourthCorrectIcon(index)
        showCorrectText(index)
    }

    private fun showFourthCorrectIcon(index: Int){
        val imageView = optionItemList[index].findViewById<ImageView>(R.id.box_card_selection_option_elixir)
        imageView.setImageResource(R.drawable.circle_correct_4)
    }
}