package com.zaen.testly.activities

import android.os.Bundle
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity


class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_about
        super.onCreate(savedInstanceState)
        val toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        toolbar?.setDisplayShowTitleEnabled(true)
    }

}
