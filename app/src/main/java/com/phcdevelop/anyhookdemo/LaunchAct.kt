package com.phcdevelop.anyhookdemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.fragment.app.FragmentActivity

/**
 * @Author PHC
 * @Data 2022/5/9 17:36
 */
class LaunchAct:FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(text = "no hook")
        }
    }
}