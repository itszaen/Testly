package com.zaen.testly.utils

class Common {
    fun allNotNull(vararg objects: Any?) :Boolean {
        for (o: Any? in objects){
            if (o != null){
                return false
            }
        }
        return true
    }

    fun getTimestamp(): Long{
        return System.currentTimeMillis() / 1000L
    }
}