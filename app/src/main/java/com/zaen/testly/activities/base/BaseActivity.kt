package com.zaen.testly.activities.base

import android.os.Bundle
import butterknife.ButterKnife
import butterknife.Unbinder
import com.zaen.testly.utils.LogUtils
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by zaen on 2/27/18.
 */

/*
* layoutRes
* NO unbinder
* */

abstract class BaseActivity : SupportActivity(){
    protected var unbinder: Unbinder? = null
    protected var layoutRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.log(this,2,"onCreate")
        super.onCreate(savedInstanceState)
        if (layoutRes != null) {
            setContentView(layoutRes!!)
            unbinder = ButterKnife.bind(this)
        } else {
            LogUtils.failure(this,5,"No layout resource specified, not inflating.")
        }
    }

    override fun onStart() {
        LogUtils.log(this,2,"onStart")
        super.onStart()
    }

    override fun onDestroy() {
        LogUtils.log(this,2, "onDestroy")
        unbinder?.unbind()
        super.onDestroy()
    }
}