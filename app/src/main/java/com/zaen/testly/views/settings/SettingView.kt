package com.zaen.testly.views.settings

import android.content.Context
import android.graphics.drawable.Icon
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

class SettingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
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
        inflater.inflate(R.layout.view_setting, this)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingView)
        iconString = a.getString(R.styleable.SettingView_settingIcon)
        titleRes = a.getResourceId(R.styleable.SettingView_settingTitle, 0)
        captionRes = a.getResourceId(R.styleable.SettingView_settingCaption, 0)
        val minimumApi = a.getInteger(R.styleable.SettingView_settingMinApi, 0)
        a.recycle()

        if (Build.VERSION.SDK_INT < minimumApi) visibility = View.GONE
    }

    override fun onFinishInflate() {
        ButterKnife.bind(this)

        icon.setIcon(icon.getIcon().icon(iconString))
        title.setText(titleRes)
        caption.setText(captionRes)

        /*
        setPadding((int) getResources().getDimension(R.dimen.medium_spacing), 0, (int) getResources().getDimension(R.dimen.medium_spacing), 0);
        setMinimumHeight((int) getResources().getDimension(R.dimen.listitem_height_twoline));
        */
        super.onFinishInflate()
    }

}
