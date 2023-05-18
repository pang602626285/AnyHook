package com.phcdevelop.anyhookdemo

import android.app.Application
import com.phcdevelop.preview_hook_api.utils.PreviewHookCheck

/**
 * @Author PHC
 * @Data 2022/5/5 16:27
 */
class MApp: Application() {
    override fun onCreate() {
        super.onCreate()
        com.phcdevelop.preview_hook_api.utils.PreviewHookCheck.checkOnAppOnCreate(this)
    }
}