package com.phcdevelop.anyhook.hook_interface

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity

interface HookInterface {

    fun init(App:Application,replaceActClaz: Class<out ComponentActivity>)

    fun onActCreate(activity: ComponentActivity)

}