package com.zaen.testly.utils

import android.util.Log
import android.util.Log.*
import com.zaen.testly.BuildConfig

class LogUtils {
    companion object {
        private const val MAX_TAG_SIZE = 23
        private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID

        fun log(obj: Any, level: Int, message: String){
            when (level){
                VERBOSE -> Log.v(TAG(obj), "[Success]$message")
                DEBUG -> Log.d(TAG(obj), "[Success]$message")
                INFO -> Log.i(TAG(obj), "[Success]$message")
                WARN -> Log.w(TAG(obj), "[Success]$message")
                ERROR -> Log.e(TAG(obj), "[Success]$message")
                ASSERT -> Log.wtf(TAG(obj), "[Success]$message")
            }
        }

        fun success(obj: Any, level: Int, message: String){
            when (level){
                VERBOSE -> Log.v(TAG(obj), "[Success]$message")
                DEBUG -> Log.d(TAG(obj), "[Success]$message")
                INFO -> Log.i(TAG(obj), "[Success]$message")
                WARN -> Log.w(TAG(obj), "[Success]$message")
                ERROR -> Log.e(TAG(obj), "[Success]$message")
                ASSERT -> Log.wtf(TAG(obj), "[Success]$message")
            }
        }

        fun failure(obj: Any, level: Int, message: String){
            when (level){
                VERBOSE -> Log.v(TAG(obj), "[Failure]$message")
                DEBUG -> Log.d(TAG(obj), "[Failure]$message")
                INFO -> Log.i(TAG(obj), "[Failure]$message")
                WARN -> Log.w(TAG(obj), "[Failure]$message")
                ERROR -> Log.e(TAG(obj), "[Failure]$message")
                ASSERT -> Log.wtf(TAG(obj), "[Failure]$message")
            }
        }

        fun failure(obj: Any, level: Int, message: String, exception: Exception){
            when (level){
                VERBOSE -> Log.v(TAG(obj), "[Failure]$message Exception: $exception")
                DEBUG -> Log.d(TAG(obj), "[Failure]$message Exception: $exception")
                INFO -> Log.i(TAG(obj), "[Failure]$message Exception: $exception")
                WARN -> Log.w(TAG(obj), "[Failure]$message Exception: $exception")
                ERROR -> Log.e(TAG(obj), "[Failure]$message Exception: $exception")
                ASSERT -> Log.wtf(TAG(obj), "[Failure]$message Exception: $exception")
            }
        }

        fun TAG(obj: Any): String {
            val objName = obj.javaClass.simpleName
            return if (objName.length > MAX_TAG_SIZE) {
                objName.substring(0, MAX_TAG_SIZE)
            } else {
                objName
            }
        }

        fun CLASS_LINE(): String {
            try {
                val elements = Thread.currentThread().stackTrace
                for (ste in elements) {
                    var cls = ste.className
                    if (cls.startsWith(PACKAGE_NAME)) {
                        cls = cls.replace(PACKAGE_NAME, "")
                        return cls + "." + ste.methodName + "(" + ste.lineNumber + ")"
                    }
                }
            } catch (e: Exception) {
                return "null"
            }
            return "null"
        }

        fun METHOD_INFO(): String {

            try {
                val elements = Thread.currentThread().stackTrace
                for (ste in elements) {
                    // Need \n to link
                    if (ste.className.startsWith(PACKAGE_NAME)) return ste.toString() + "\n"
                }
            } catch (e: Exception) {
                return "null"
            }

            return "null"
        }

        fun CALLED_BY(): String {
            try {
                val elements = Thread.currentThread().stackTrace

                var breakFlg = false
                for (ste in elements) {
                    var cls = ste.className
                    if (breakFlg) {
                        cls = cls.replace(PACKAGE_NAME, "")
                        return cls + "." + ste.methodName + "(" + ste.lineNumber + ")"
                    }
                    if (cls.startsWith(PACKAGE_NAME)) breakFlg = true
                }
            } catch (e: Exception) {
                return "null"
            }

            return "null"
        }
    }
}