package com.zaen.testly.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.fragments.cas.CreateSetFragment
import kotlinx.android.synthetic.main.activity_create_set.*

class CreateSetActivity : BaseActivity(){
    var toolbar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_create_set
        super.onCreate(savedInstanceState)

        toolbar = this.supportActionBar

        if (fragment_container_create_set != null){
            if (savedInstanceState != null){
                return
            }
            loadRootFragment(R.id.fragment_container_create_set,CreateSetFragment(),true,true)
        }
    }


}
