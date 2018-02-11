package com.zaen.testly.utils.preferences

import android.content.Context
import android.content.SharedPreferences

internal class SharedPrefs/* package */(context: Context) {

    private val sharedPrefs: SharedPreferences

    private val editor: SharedPreferences.Editor
        get() = sharedPrefs.edit()

    init {
        sharedPrefs = context.applicationContext
                .getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE)
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    fun put(key: String, value: Int) {
        editor.putInt(key, value).commit()
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return sharedPrefs.getBoolean(key, defaultValue)
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }

    companion object {

        private val PREFERENCES_NAME = "com.zaen.testly.SHARED_PREFS"
        private val PREFERENCES_MODE = Context.MODE_PRIVATE
    }
}
