package com.phcdevelop.anyhook.hook

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * @Author PHC
 * @Data 2022/3/3 17:55
 */
object PreviewHook {
    /**
     * 需要在activity 的onCreate方法中调用
     */
    @JvmStatic
    fun onActCreate(activity: Activity,savedInstanceState: Bundle?) {
        //no op
    }

    /**
     * @param app App实例
     * @param replaceActClaz 需要替换启动的activity类
     */
    @JvmStatic
    fun init(app: Application, replaceActClaz: Class<out ComponentActivity>) {
        //no op
    }

}