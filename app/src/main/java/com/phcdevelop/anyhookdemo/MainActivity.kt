package com.phcdevelop.anyhookdemo

import android.os.Bundle
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.phcdevelop.anyhook.hook.PreviewHook
import com.phcdevelop.anyhookdemo.ui.theme.AnyHookDemoTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreviewHook.onActCreate(this,savedInstanceState)
        Toast.makeText(this,"hook success!!",Toast.LENGTH_LONG).show()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnyHookDemoTheme {
        Greeting("Android")
    }
}