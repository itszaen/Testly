package com.zaen.testly.settings.views

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsImageView
import com.zaen.testly.R
import kotterknife.bindView

class SettingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val iconString: String?
    @StringRes
    private val titleRes: Int
    @StringRes
    private val captionRes: Int

    val iconView: IconicsImageView by bindView(R.id.icon)
    val title: TextView by bindView(R.id.title)
    val caption: TextView by bindView(R.id.caption)

    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.item_setting, this)

        val attr = getContext().obtainStyledAttributes(attrs, R.styleable.SettingView)
        iconString = attr.getString(R.styleable.SettingView_cardItemIcon)
        titleRes = attr.getResourceId(R.styleable.SettingView_cardItemTitle, 0)
        captionRes = attr.getResourceId(R.styleable.SettingView_cardItemCaption, 0)
        attr.recycle()
    }

    override fun onFinishInflate() {
        iconView.icon = iconView.icon.icon(iconString)
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
