package com.zaen.testly.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity

class CreateCardActivity : BaseActivity() {

    var toolbar : ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_card
        super.onCreate(savedInstanceState)

        toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)
    }
}
