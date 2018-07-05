package com.zaen.testly.cas.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.zaen.testly.R
import com.zaen.testly.data.CardData
import java.util.*

abstract class CardSelectionBaseView(context: Context, open val card: CardData, protected val container: FrameLayout) : CardView(context) {
    interface OptionClickListener{
        fun onRightOptionClick(view: View)
        fun onWrongOptionClick(view: View)
    }
    internal val optionItemList = arrayListOf<mehdi.sakout.fancybuttons.FancyButton>()
    private var hasInflated = false
    private val cardSelectionView= inflater.inflate(R.layout.view_card_selection,container,false) as android.support.v7.widget.CardView
    private val questionTextView: android.support.v7.widget.AppCompatTextView
    protected val optionContainer: LinearLayout

    protected val rightIcon = IconicsDrawable(context)
            .icon(GoogleMaterial.Icon.gmd_done)
            .color(ContextCompat.getColor(context, R.color.accent_green))
            .sizeDp(16)
    protected val wrongIcon = IconicsDrawable(context)
            .icon(GoogleMaterial.Icon.gmd_clear)
            .color(ContextCompat.getColor(context, R.color.accent_red))
            .sizeDp(16)

    init {
        container.addView(cardSelectionView)
        questionTextView = cardSelectionView.findViewById(R.id.text_card_selection_question)
        optionContainer = cardSelectionView.findViewById(R.id.option_container_card_selection)
    }

    fun inflate(options: ArrayList<String>){
        if (hasInflated){ return }
        hasInflated = true
        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, optionContainer,false)
                    as mehdi.sakout.fancybuttons.FancyButton
            optionItemList.add(optionItemLayout)
            optionItemLayout.setText(option)
            optionContainer.addView(optionItemLayout)
        }
    }

    protected fun disableOption(){
        for ((i,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener(null)
            optionItemLayout.isClickable = false
            optionItemLayout.isFocusable = false
        }
    }

    abstract fun showAnswer()

}