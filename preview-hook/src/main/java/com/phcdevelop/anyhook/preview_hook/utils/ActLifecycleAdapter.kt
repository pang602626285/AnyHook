package com.phcdevelop.anyhook.preview_hook.utils

import android.app.Application
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * @Author PHC
 * @Data 2023/5/15 23:24
 */
class ActLifecycleAdapter {
    companion object {
        val proxy: Application.ActivityLifecycleCallbacks = Proxy.newProxyInstance(
            this::class.java.classLoader,
            arrayOf(Application.ActivityLifecycleCallbacks::class.java)
        ) { proxy, method, args -> } as Application.ActivityLifecycleCallbacks
    }
}