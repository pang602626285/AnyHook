package com.phcdevelop.anyhook.preview_hook.provider

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import com.phcdevelop.anyhook.preview_hook.hook.PreviewHook
import com.phcdevelop.anyhook.preview_hook.utils.ActLifecycleAdapter
import com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback
import com.phcdevelop.anyhook.utils.ThreadUtils
import com.phcdevelop.preview_hook_annotation.PreviewCreateAct
import java.util.*


class PreviewHookProvider : CPAdapter() {
    companion object{
        const val PREVIEW_ACT_NAME = "PREVIEW_HOOK_ACT_NAME"
        const val COMPOSE_VERSION = "COMPOSE_VERSION"
    }


    override fun attachInfo(context: Context, info: ProviderInfo) {
//        super.attachInfo(context, info)
        val hookName = ServiceLoader.load(PreviewCreateAct::class.java).iterator().next().javaClass.canonicalName//获取名字
        hookName?.takeIf { it.isNotEmpty() }?.let { actName->
            PreviewHook.instance.init(context as Application,
                Class.forName(actName) as Class<out ComponentActivity>
            )
            (context as? Application)?.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks by ActLifecycleAdapter.proxy{
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    when{
                        activity is ComponentActivity && activity is AsyncCallback ->{
                            activity.doAsync(activity) {
                                ThreadUtils.isMain {
                                    PreviewHook.instance.onActCreate(activity)
                                }?: kotlin.run {
                                    throw Exception("Must run on main thread")
                                }
                            }
                        }
                        activity is ComponentActivity && activity.javaClass.name.equals(actName)->{
                            PreviewHook.instance.onActCreate(activity)
                        }
                    }
                }
            })
        }
    }
}