package com.phcdevelop.anyhook.utils

import android.os.Looper
object ThreadUtils {

    fun <T> isMain(doMain:()->T):T?{
        return if(Looper.getMainLooper() == Looper.myLooper()){
            doMain()
        }else{
            null
        }
    }

}