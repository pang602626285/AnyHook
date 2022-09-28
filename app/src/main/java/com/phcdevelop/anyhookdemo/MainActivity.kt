package com.phcdevelop.anyhookdemo

import android.os.Bundle
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.fragment.app.FragmentActivity
import com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback
import com.phcdevelop.anyhookdemo.ui.theme.AnyHookDemoTheme
import kotlin.concurrent.thread

class MainActivity : FragmentActivity(),AsyncCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "hook success!!", Toast.LENGTH_LONG).show()

    }

    override fun doAsync(doOnCreate: () -> Unit) {
        thread {
            Thread.sleep(3000)
                doOnCreate()
        }
    }
}

@Preview
@Composable
fun Greeting(@PreviewParameter(TestProvider::class) name: String="123") {
    Text(text = "Hello $name!")

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnyHookDemoTheme {
        Greeting(LocalContext.current.javaClass.name)
    }
}

class TestProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("test", "123")

}
