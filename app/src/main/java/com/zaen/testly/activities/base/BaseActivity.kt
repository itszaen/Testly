package com.zaen.testly.activities.base

import android.os.Bundle
import butterknife.ButterKnife
import butterknife.Unbinder
import com.afollestad.materialdialogs.MaterialDialog
import com.zaen.testly.R
import com.zaen.testly.utils.LogUtils
import de.mateware.snacky.Snacky
import me.yokeyword.fragmentation.SupportActivity
import java.lang.Exception

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

    protected fun snackySuccess(text: String){
        Snacky.builder()
                .setActivity(this)
                .setText(text)
                .success()
                .setDuration(Snacky.LENGTH_LONG)
                .show()
    }

    protected fun snackyInfo(text: String){
        Snacky.builder()
                .setActivity(this)
                .setText(text)
                .info()
                .setDuration(Snacky.LENGTH_LONG)
                .show()
    }

    protected fun snackyWarning(warning: String){
        Snacky.builder()
                .setActivity(this)
                .setText(warning)
                .setDuration(Snacky.LENGTH_LONG)
                .warning()
                .show()
    }

    protected fun snackyFailure(error: String){
        Snacky.builder()
                .setActivity(this)
                .setText(
                        "$error"
                )
                .setDuration(Snacky.LENGTH_LONG)
                .error()
                .show()
    }

    protected fun snackyException(error: String, e: Exception){
        Snacky.builder()
                .setActivity(this)
                .setText(
                        "$error\nClick OPEN to see exception."
                )
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("OPEN")
                .setActionClickListener {
                    MaterialDialog.Builder(this)
                            .title("Exception")
                            .content(e.toString())
                            .positiveText(R.string.react_positive)
                            .show()
                }
                .error()
                .show()
    }
}