package com.zaen.testly.cas.children.createPool.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.cas.children.createPool.fragments.CreatePoolFragment
import kotlinx.android.synthetic.main.activity_create_set.*

class CreatePoolActivity : BaseActivity() {
    var toolbar : ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_pool
        super.onCreate(savedInstanceState)

        toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
        toolbar?.setDisplayShowTitleEnabled(true)

        if (fragment_container_create_set != null){
            if (savedInstanceState != null){
                return
            }
            loadRootFragment(R.id.fragment_container_create_pool, CreatePoolFragment(),true,true)
        }
    }


}