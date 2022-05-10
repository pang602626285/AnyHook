package com.phcdevelop.anyhook.preview_hook_check.until

import android.app.Application
import com.phcdevelop.anyhook.until.method


/**
 * 基于PreviewActivity中代码，通过放射进行调用
 */
object PreviewHookCheck {

    /**
     * 在app onCreate方法中调用，防止previewHook被别的框架替换掉Handler中的callback
     */
    @JvmStatic
    fun checkOnAppOnCreate(app:Application){
        "com.phcdevelop.anyhook.preview_hook.hook.PreviewHook".method("getInstance")?.invoke(null)?.let { instance->
            Class.forName("com.phcdevelop.anyhook.preview_hook.hook.PreviewHook").getMethod("init",Application::class.java).invoke(instance,app)
        }
    }
}