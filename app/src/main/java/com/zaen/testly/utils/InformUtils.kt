package com.zaen.testly.utils

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.zaen.testly.R
import de.mateware.snacky.Snacky
import java.lang.Exception

class InformUtils(val activity: AppCompatActivity){
    fun snackySuccess(text: String){
        Snacky.builder()
                .setActivity(activity)
                .setText(text)
                .success()
                .setDuration(Snacky.LENGTH_LONG)
                .show()
    }

    fun snackyInfo(text: String){
        Snacky.builder()
                .setActivity(activity)
                .setText(text)
                .info()
                .setDuration(Snacky.LENGTH_LONG)
                .show()
    }

    fun snackyWarning(warning: String){
        Snacky.builder()
                .setActivity(activity)
                .setText(warning)
                .setDuration(Snacky.LENGTH_LONG)
                .warning()
                .show()
    }

    fun snackyFailure(error: String){
        Snacky.builder()
                .setActivity(activity)
                .setText(
                        error
                )
                .setDuration(Snacky.LENGTH_LONG)
                .error()
                .show()
    }

    fun snackyException(error: String, e: Exception){
        Snacky.builder()
                .setActivity(activity)
                .setText(
                        "$error\nClick OPEN to see exception."
                )
                .setDuration(Snacky.LENGTH_LONG)
                .setActionText("OPEN")
                .setActionClickListener {
                    MaterialDialog.Builder(activity)
                            .title("Exception")
                            .content(e.toString())
                            .positiveText(R.string.react_positive)
                            .show()
                }
                .error()
                .show()
    }

    fun notImplemented(){
        snackyInfo("This feature is not implemented yet.")
        LogUtils.failure(activity, Log.INFO, "The feature user requested is not implemented yet.")
    }
}