package com.zaen.testly.activities

import android.os.Bundle
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity

class CreateSetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_set
        super.onCreate(savedInstanceState)
    }
}
