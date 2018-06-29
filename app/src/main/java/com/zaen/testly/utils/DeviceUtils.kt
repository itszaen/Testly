package com.zaen.testly.utils

import android.content.Context

class ScreenPropUtils(val context: Context){
    fun getWidth() : Float{
        when(context.resources.displayMetrics.density){
            0.75F -> return 200F
            1.0F -> return 320F
            1.5F -> return 480F
            2.0F -> return 720F
            3.0F -> return 960F
            4.0F -> return 1440F
        }
        return 0F
    }
    fun getHeight() : Float{
        when(context.resources.displayMetrics.density){
            0.75F -> return 320F
            1.0F -> return 480F
            1.5F -> return 800F
            2.0F -> return 1280F
            3.0F -> return 1600F
            4.0F -> return 2560F
        }
        return 0F
    }
    fun getOrientation() : Int{
        return context.resources.configuration.orientation
        // ORIENTATION_UNDEFINED = 0
        // ORIENTATION_PORTRAIT = 1
        // ORIENTATION_LANDSCAPE = 2
    }
}