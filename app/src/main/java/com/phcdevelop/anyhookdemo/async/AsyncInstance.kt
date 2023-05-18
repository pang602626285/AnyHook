package com.phcdevelop.anyhookdemo.async

import androidx.activity.ComponentActivity
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplClazz
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplField
import com.phcdevelop.preview_hook_api.async.AsyncCallback

/**
 * @Author PHC
 * @Data 2023/5/15 11:19
 */
@PreviewAsyncImplClazz
class AsyncInstance {

    companion object {
        @PreviewAsyncImplField
        val instance: AsyncCallback = object : AsyncCallback {
            override fun doAsync(activity: ComponentActivity,doOnCreate: () -> Unit) {
                //执行同步或异步任务后调用doOnCreate
                doOnCreate()
            }

        }
    }
}