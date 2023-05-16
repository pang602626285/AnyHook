package com.phcdevelop.anyhookdemo.async

import com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplClazz
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplGetter

/**
 * @Author PHC
 * @Data 2023/5/15 11:19
 */
class AsyncInstance {

    @PreviewAsyncImplClazz
    companion object {
        @get:PreviewAsyncImplGetter
        val instance: AsyncCallback = object : AsyncCallback {
            override fun doAsync(doOnCreate: () -> Unit) {
                //执行同步或异步任务后调用doOnCreate
                doOnCreate()
            }

        }
    }
}