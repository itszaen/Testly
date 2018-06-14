package com.zaen.testly.utils

import android.content.Context
import android.util.DisplayMetrics

class CommonUtils {
    companion object {
        fun allNotNull(vararg objects: Any?): Boolean {
            for (o: Any? in objects) {
                if (o != null) {
                    return false
                }
            }
            return true
        }

        fun getTimestamp(): Long {
            return System.currentTimeMillis() / 1000L
        }

        fun getDisplayMetrics(context: Context): DisplayMetrics {
            return context.resources.displayMetrics
        }

        fun dpToPx(context: Context, dp: Float): Int {
            return Math.round(dp * getDisplayMetrics(context).density)
        }
    }
}