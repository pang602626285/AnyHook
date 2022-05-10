package com.phcdevelop.anyhook.preview_hook.provider

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import com.phcdevelop.anyhook.preview_hook.hook.PreviewHook


class PreviewHookProvider : FileProvider() {
    companion object{
        const val PREVIEW_ACT_NAME = "PREVIEW_HOOK_ACT_NAME"
    }


    override fun attachInfo(context: Context, info: ProviderInfo) {
//        super.attachInfo(context, info)
        val hookName = context.packageManager.getApplicationInfo(context.packageName,PackageManager.GET_META_DATA).metaData.getString(PREVIEW_ACT_NAME)
        hookName?.takeIf { it.isNotEmpty() }?.let { actName->
            PreviewHook.instance.init(context as Application,
                Class.forName(actName) as Class<out ComponentActivity>
            )
            (context as? Application)?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    activity.takeIf { it is ComponentActivity && it.javaClass.name.equals(actName)}?.let {
                        PreviewHook.instance.onActCreate(it as ComponentActivity)
                    }
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }
            })
        }
    }
}