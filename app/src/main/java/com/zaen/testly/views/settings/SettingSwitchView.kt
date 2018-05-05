package com.zaen.testly.views.settings

import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.view.IconicsImageView
import com.zaen.testly.R
import com.zaen.testly.utils.preferences.Prefs

class SettingSwitchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val iconString: String?
    private val preferenceKey: String
    @StringRes
    private val titleRes: Int
    @StringRes
    private val captionRes: Int
    private val defaultValue: Boolean
    @BindView(R.id.icon)
    lateinit var icon: IconicsImageView
    @BindView(R.id.title)
    lateinit var title: TextView
    @BindView(R.id.caption)
    lateinit var caption: TextView
    @BindView(R.id.toggle)
    lateinit var toggle: SwitchCompat
    private var clickListener: View.OnClickListener? = null

    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.item_setting_switch, this)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingSwitchView)
        iconString = a.getString(R.styleable.SettingSwitchView_cardItemIcon)
        val prefKeyRes = a.getResourceId(R.styleable.SettingSwitchView_settingPreferenceKey, 0)
        if (prefKeyRes == 0) throw IllegalArgumentException("Invalid preference reference")
        preferenceKey = resources.getString(prefKeyRes)
        titleRes = a.getResourceId(R.styleable.SettingSwitchView_cardItemTitle, 0)
        captionRes = a.getResourceId(R.styleable.SettingSwitchView_cardItemCaption, 0)
        defaultValue = a.getBoolean(R.styleable.SettingSwitchView_settingDefaultValue, false)
        val minimumApi = 0//a.getInteger(R.styleable.SettingWithSwitchView_settingMinApi, 0);
        a.recycle()

        if (Build.VERSION.SDK_INT < minimumApi) visibility = View.GONE
    }

    override fun onFinishInflate() {
        ButterKnife.bind(this)

        icon.icon = icon.icon.icon(iconString)
        title.setText(titleRes)
        caption.setText(captionRes)
        toggle.isChecked = isChecked
        super.setOnClickListener(this)

        /*
        setPadding((int) getResources().getDimension(R.dimen.medium_spacing), 0,(int) getResources().getDimension(R.dimen.medium_spacing), 0);
        setMinimumHeight((int) getResources().getDimension(R.dimen.listitem_height_twoline));
        */
        super.onFinishInflate()
    }

    override fun setOnClickListener(clickListener: View.OnClickListener?) {
        this.clickListener = clickListener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onClick(view: View) {
        toggle()
        if (clickListener != null) clickListener!!.onClick(this)
    }

    private val isChecked: Boolean
        get() = Prefs.getToggleValue(preferenceKey, defaultValue)

    fun toggle() {
        Prefs.setToggleValue(preferenceKey, !isChecked)
        val checked = isChecked
        toggle.isChecked = checked
    }

    fun setTitleText(text: String){
        title.text = text
    }

    fun setCaptionText(text: String){
        caption.text = text
    }

}
