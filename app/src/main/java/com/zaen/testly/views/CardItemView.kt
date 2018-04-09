package com.zaen.testly.views

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.view.IconicsImageView
import com.zaen.testly.R

class CardItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val iconString: String?
    @StringRes
    private val titleRes: Int
    @StringRes
    private val captionRes: Int
    @BindView(R.id.icon)
    lateinit var icon: IconicsImageView
    @BindView(R.id.title)
    lateinit var title: TextView
    @BindView(R.id.caption)
    lateinit var caption: TextView

    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.view_item_card, this)

        val attr = getContext().obtainStyledAttributes(attrs, R.styleable.CardItemView)
        iconString = attr.getString(R.styleable.CardItemView_cardItemIcon)
        titleRes = attr.getResourceId(R.styleable.CardItemView_cardItemTitle, 0)
        captionRes = attr.getResourceId(R.styleable.CardItemView_cardItemCaption, 0)
        attr.recycle()
    }

    override fun onFinishInflate() {
        ButterKnife.bind(this)

        icon.icon = icon.icon.icon(iconString)
        title.setText(titleRes)
        caption.setText(captionRes)

        /*
        setPadding((int) getResources().getDimension(R.dimen.medium_spacing), 0, (int) getResources().getDimension(R.dimen.medium_spacing), 0);
        setMinimumHeight((int) getResources().getDimension(R.dimen.listitem_height_twoline));
        */
        super.onFinishInflate()
    }

    fun setTitleText(text: String){
        title.text = text
    }

    fun setCaptionText(text: String){
        caption.text = text
    }

}