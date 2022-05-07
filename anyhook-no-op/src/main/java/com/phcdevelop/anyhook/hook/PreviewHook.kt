package com.phcdevelop.anyhook.hook

import android.app.Application
import androidx.activity.ComponentActivity
import com.phcdevelop.anyhook.hook_interface.HookInterface

/**
 * @Author PHC
 * @Data 2022/3/3 17:55
 */
class PreviewHook private constructor() : HookInterface {
    companion object{
        val instance: HookInterface by lazy { PreviewHook() }
    }

    /**
     * 需要在activity 的onCreate方法中调用
     */
    override fun onActCreate(activity: ComponentActivity) {
        //no op
    }

    /**
     * @param app App实例
     * @param replaceActClaz 需要替换启动的activity类
     */
    override fun init(app: Application, replaceActClaz: Class<out ComponentActivity>) {
        //no op
    }

}