package com.phcdevelop.anyhookdemo

import android.app.Application
import com.phcdevelop.anyhook.preview_hook_check.until.PreviewHookCheck

/**
 * @Author PHC
 * @Data 2022/5/5 16:27
 */
class MApp: Application() {
    override fun onCreate() {
        super.onCreate()
        PreviewHookCheck.checkOnAppOnCreate(this)
    }
}