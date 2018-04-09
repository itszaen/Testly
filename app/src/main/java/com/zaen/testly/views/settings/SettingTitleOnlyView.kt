package com.zaen.testly.views.settings

import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.view.IconicsImageView
import com.zaen.testly.R

class SettingTitleOnlyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val iconString: String?
    @StringRes
    private val titleRes: Int
    @BindView(R.id.icon)
    lateinit var icon: IconicsImageView
    @BindView(R.id.title)
    lateinit var title: TextView

    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.view_setting_titleonly, this)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingTitleOnlyView)
        iconString = a.getString(R.styleable.SettingTitleOnlyView_cardItemIcon)
        titleRes = a.getResourceId(R.styleable.SettingTitleOnlyView_cardItemTitle, 0)
        val minimumApi = a.getInteger(R.styleable.SettingTitleOnlyView_settingMinApi, 0)
        a.recycle()

        if (Build.VERSION.SDK_INT < minimumApi) visibility = View.GONE
    }

    override fun onFinishInflate() {
        ButterKnife.bind(this)

        icon.setIcon(icon.getIcon().icon(iconString))
        title.setText(titleRes)

        /*
        setPadding((int) getResources().getDimension(R.dimen.medium_spacing), 0, (int) getResources().getDimension(R.dimen.medium_spacing), 0);
        setMinimumHeight((int) getResources().getDimension(R.dimen.listitem_height_twoline));
        */
        super.onFinishInflate()
    }

    fun setTitleText(text: String){
        title.text = text
    }

}