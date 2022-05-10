package com.phcdevelop.anyhook.preview_hook.hook_interface

import android.app.Application
import androidx.activity.ComponentActivity

interface HookInterface {

    fun init(app:Application)

    fun init(app:Application,replaceActClaz: Class<out ComponentActivity>)

    fun onActCreate(activity: ComponentActivity)

}