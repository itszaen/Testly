package com.zaen.testly.utils.preferences

import android.content.Context

object Prefs {

    private var sharedPrefs: SharedPrefs? = null

    /**
     * Initialise the Prefs object for future static usage.
     * Make sure to initialise this in Application class.
     *
     * @param context The context to initialise with.
     */
    fun init(context: Context) {
        if (sharedPrefs != null) {
            throw RuntimeException("Prefs has already been instantiated")
        }
        sharedPrefs = SharedPrefs(context)
    }
    /**
     * Should display video files in media.
     */
    fun showVideos(): Boolean {
        return prefs.get(Keys.SHOW_VIDEOS, Defaults.SHOW_VIDEOS)
    }

    fun sendNotification(value: Boolean){
        prefs.put(Keys.SEND_NOTIFICATION, Defaults.SEND_NOTIFICATION)
    }

    fun setShowDevbar(value: Boolean){
        prefs.put(Keys.SHOW_DEVBAR, Defaults.SHOW_DEVBAR)
    }


    fun setSendNotification(value: Boolean){
        prefs.put(Keys.SEND_NOTIFICATION, value)
    }

    // ***** TODO [!] Calvin: These methods does not belong here, DO NOT expose generic methods to clients

    @Deprecated("")
    fun setToggleValue(key: String, value: Boolean) {
        prefs.put(key, value)
    }

    @Deprecated("")
    fun getToggleValue(key: String, defaultValue: Boolean): Boolean {
        return prefs.get(key, defaultValue)
    }
    // ***** Remove these methods when SettingWithSwitchView is refactored.

    private val prefs: SharedPrefs
        get() {
            if (sharedPrefs == null) {
                throw RuntimeException("Prefs has not been instantiated. Call init() with context")
            }
            return sharedPrefs as SharedPrefs
        }
}