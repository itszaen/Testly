package com.zaen.testly.activities.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife
import butterknife.Unbinder
import com.zaen.testly.utils.LogUtils.Companion.TAG
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by zaen on 2/27/18.
 */

abstract class BaseActivity : SupportActivity(){
    companion object {
    }

    protected var unbinder: Unbinder? = null
    protected var layoutRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(TAG(this),"onCreate")
        super.onCreate(savedInstanceState, persistentState)
        if (layoutRes != null) {
            setContentView(layoutRes!!)
            unbinder = ButterKnife.bind(this)
        } else {
            Log.w(TAG(this),"No layout resource specified, not inflating.")
        }

    }

    override fun onStart() {
        Log.d(TAG(this),"onStart")
        super.onStart()
    }

    override fun onDestroy() {
        Log.d(TAG(this),"onDestroy")
        super.onDestroy()
        unbinder?.unbind()
    }
}