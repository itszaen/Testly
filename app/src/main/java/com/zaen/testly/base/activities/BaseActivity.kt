package com.zaen.testly.base.activities

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import butterknife.ButterKnife
import butterknife.Unbinder
import com.zaen.testly.App
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
        App.addActivityToStack(this)
    }

    override fun onStart() {
        LogUtils.log(this,2,"onStart")
        super.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Up button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities()
                } else {
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        LogUtils.log(this,2, "onDestroy")
        unbinder?.unbind()
        App.removeActivityFromStack(this)
        super.onDestroy()
    }
}