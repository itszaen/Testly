package com.zaen.testly.base.activities

import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.zaen.testly.App
import com.zaen.testly.Global
import com.zaen.testly.utils.LogUtils
import com.zaen.testly.utils.ScreenPropUtils
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by zaen on 2/27/18.
 */

/*
* layoutRes
* NO unbinder
* */

abstract class BaseActivity : SupportActivity(){
    protected var layoutRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.log(this,2,"onCreate")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (ScreenPropUtils(this).getOrientation() == ORIENTATION_LANDSCAPE){
            finish()
            return
        }
        super.onCreate(savedInstanceState)
        if (layoutRes != null) {
            setContentView(layoutRes!!)
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
        App.removeActivityFromStack(this)
        super.onDestroy()
    }

    protected fun informActivityLifeCycle(cycle: String){
        val intent = Intent()
        intent.action = Global.KEY_ACTION_INFORM_LIFECYCLE_ACTIVITY
        intent.putExtra(cycle,true)
        intent.putExtras(intent)
//        LocalBroadcastManager.getInstance(this).
        this.sendBroadcast(intent)
    }
}