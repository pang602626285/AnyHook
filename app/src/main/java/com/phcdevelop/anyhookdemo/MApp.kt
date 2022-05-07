package com.phcdevelop.anyhookdemo

import android.app.Application
import com.phcdevelop.anyhook.hook.PreviewHook

/**
 * @Author PHC
 * @Data 2022/5/5 16:27
 */
class MApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreviewHook.instance.init(this,MainActivity::class.java)
    }
}