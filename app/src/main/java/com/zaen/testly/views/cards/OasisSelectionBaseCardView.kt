package com.zaen.testly.views.cards

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

abstract class OasisSelectionBaseCardView(context: Context, open val card: CardData, protected val container: FrameLayout) : CardView(context),
ISelectionBaseCardView{
    interface OptionClickListener{
        fun onRightOptionClick(view: View)
        fun onWrongOptionClick(view: View)
    }
    internal val optionItemList = arrayListOf<mehdi.sakout.fancybuttons.FancyButton>()
    override var hasInflated = false
    private val oasis= inflater.inflate(R.layout.view_card_selection_oasis,container,false)
            as android.support.v7.widget.CardView
    internal val cardObjectiveText: android.support.v7.widget.AppCompatTextView
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
        container.addView(oasis)
        cardObjectiveText = oasis.findViewById(R.id.objective_card)
        questionTextView = oasis.findViewById(R.id.text_card_selection_question)
        optionContainer = oasis.findViewById(R.id.option_container_card_selection)
    }

    override fun inflate(options: ArrayList<String>){
        if (hasInflated){ return }

        // Question
        questionTextView.text = card.question

        // Options
        for ((index, option) in options.withIndex()) {
            val optionItemLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option_oasis, optionContainer,false)
                    as mehdi.sakout.fancybuttons.FancyButton
            optionItemList.add(optionItemLayout)
            // text
            optionItemLayout.setText(option)
            // add
            optionContainer.addView(optionItemLayout)
        }
    }

    override fun disableOptions(){
        for ((i,optionItemLayout) in optionItemList.withIndex()){
            optionItemLayout.setOnClickListener(null)
            optionItemLayout.isClickable = false
            optionItemLayout.isFocusable = false
        }
    }
}