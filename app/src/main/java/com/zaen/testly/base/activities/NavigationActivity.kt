package com.zaen.testly.base.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.FrameLayout

/* Spec
onCreate
layoutRes
fragmentContainer
*/

abstract class NavigationActivity : BaseActivity(){
    companion object {

    }

    protected var inFragmentLevel = 0
    protected var fragmentContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (fragmentContainer != null){
            if (savedInstanceState != null){
                return
            }

        }
    }

}