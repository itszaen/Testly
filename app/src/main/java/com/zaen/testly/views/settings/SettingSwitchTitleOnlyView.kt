package com.zaen.testly.views.settings

import android.content.Context
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

/**
 * Created by zaen on 2/10/18.
 */
class SettingSwitchTitleOnlyView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val iconString: String?
    private val preferenceKey: String
    @StringRes
    private val titleRes: Int
    private val defaultValue: Boolean
    @BindView(R.id.icon)
    lateinit var icon: IconicsImageView
    @BindView(R.id.title)
    lateinit var title: TextView
    @BindView(R.id.toggle)
    lateinit var toggle: SwitchCompat
    private var clickListener: View.OnClickListener? = null

    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.item_setting_switch, this)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingSwitchTitleOnlyView)
        iconString = a.getString(R.styleable.SettingSwitchTitleOnlyView_cardItemIcon)
        val prefKeyRes = a.getResourceId(R.styleable.SettingSwitchTitleOnlyView_settingPreferenceKey, 0)
        if (prefKeyRes == 0) throw IllegalArgumentException("Invalid preference reference")
        preferenceKey = resources.getString(prefKeyRes)
        titleRes = a.getResourceId(R.styleable.SettingSwitchTitleOnlyView_cardItemTitle, 0)
        defaultValue = a.getBoolean(R.styleable.SettingSwitchTitleOnlyView_settingDefaultValue, false)
        a.recycle()
    }

    override fun onFinishInflate() {
        ButterKnife.bind(this)

        icon.icon = icon.icon.icon(iconString)
        title.setText(titleRes)
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

}
