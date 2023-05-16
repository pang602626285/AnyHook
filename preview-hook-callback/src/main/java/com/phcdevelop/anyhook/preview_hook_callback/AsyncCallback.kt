package com.phcdevelop.anyhook.preview_hook_callback

import androidx.activity.ComponentActivity

/**
 * 异步回调
 */
interface AsyncCallback {
    /**
     * 在里面添加需要执行的异步操作，在操作结束后，自行在主线程调用doOnCreate
     */
    fun doAsync(activity: ComponentActivity,doOnCreate:()->Unit)
}