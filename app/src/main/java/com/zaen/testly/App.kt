package com.zaen.testly

import android.app.Application
import android.content.Context
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.squareup.leakcanary.LeakCanary
import com.zaen.testly.utils.preferences.Prefs


class App : Application(){
    companion object {
        private var mInstance: App? = null
        fun getInstance(): App? {
            return mInstance
        }
    }
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        registerFontIcons()
        initialiseStorage()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(IconicsContextWrapper.wrap(base))
    }

    private fun registerFontIcons() {
        Iconics.registerFont(GoogleMaterial())
        Iconics.registerFont(CommunityMaterial())
        Iconics.registerFont(FontAwesome())
    }

    private fun initialiseStorage() {
        Prefs.init(this)
    }

}