package com.zaen.testly.utils

import com.zaen.testly.BuildConfig

class LogUtils {
    companion object {
        private val MAX_TAG_SIZE = 23
        val PACKAGE_NAME = BuildConfig.APPLICATION_ID

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
                    // 最後の改行がないとリンクされない
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