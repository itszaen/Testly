package com.zaen.testly

import android.app.Application
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.Iconics
import com.squareup.leakcanary.LeakCanary
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.base.fragments.BaseFragment
import com.zaen.testly.utils.preferences.Prefs
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.utils.Log
import io.fabric.sdk.android.Fabric
import me.yokeyword.fragmentation.Fragmentation


class App : Application(){
    companion object {
        private lateinit var mInstance: App
        fun getInstance(): App {
            return mInstance
        }

        private val activityStack: ArrayList<BaseActivity> = arrayListOf()
        fun getActivityStack(): ArrayList<BaseActivity>{
            return activityStack
        }
        fun addActivityToStack(activity: BaseActivity){
            if (!activityStack.contains(activity)){
                activityStack.add(activity)
            }
        }
        fun removeActivityFromStack(activity: BaseActivity){
            activityStack.remove(activity)
        }

        private val fragmentStack: ArrayList<BaseFragment> = arrayListOf()
        fun getFragmentStack(): ArrayList<BaseFragment>{
            return fragmentStack
        }
        fun addFragmentToStack(fragment: BaseFragment){
            if (!fragmentStack.contains(fragment)){
                fragmentStack.add(fragment)
            }
        }
        fun removeActivityFromStack(fragment: BaseFragment){
            fragmentStack.remove(fragment)
        }
    }
    override fun onCreate() {
        super.onCreate()

        // App Instance
        mInstance = this

        // Leak Canary
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }

        // Fabric Crashlytics
        Fabric.with(this)

        // Android-Iconics
        Iconics.registerFont(GoogleMaterial())
        Iconics.registerFont(CommunityMaterial())
        Iconics.registerFont(FontAwesome())

        // Initialize Storage
        Prefs.init(this)

        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()

        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
    }

}