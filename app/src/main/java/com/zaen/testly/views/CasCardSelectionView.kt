package com.zaen.testly.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.BindView
import com.zaen.testly.R
import com.zaen.testly.R.id.option_container_card_selection
import com.zaen.testly.data.SelectionCardData

class CasCardSelectionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr){
    private val attemptCount: Int?
    private val questionText: String?
    @BindView(R.id.count_attempt)
    lateinit var attemptCountView: AppCompatTextView
    @BindView(R.id.text_card_selection_question)
    lateinit var questionTextView: AppCompatTextView

    init{
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.view_card_selection, this)

        val attr = getContext().obtainStyledAttributes(attrs, R.styleable.CasCardSelectionView)
        attemptCount = attr.getInt(R.styleable.CasCardSelectionView_CasCardAttemptCount, 0)
        questionText = attr.getString(R.styleable.CasCardSelectionView_CasCardQuestion)
        attr.recycle()



        for ((i,v) in card.options.withIndex()) {
            val optionLayout = inflater.inflate(R.layout.item_layout_linear_card_selection_option, null)
            ((optionLayout as android.support.constraint.ConstraintLayout).getChildAt(0) as android.support.v7.widget.AppCompatTextView).text = v
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            optionLayout.layoutParams = params
            if (i == card.answer) {
                optionLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.md_light_blue_100))
            }
            (option_container_card_selection as LinearLayout).addView(optionLayout)
        }

    }

    override fun onFinishInflate() {
        questionTextView.text = card.question

        attemptCountView.text = "Attemp Count: "+attemptCount.toString()
        super.onFinishInflate()
    }

}