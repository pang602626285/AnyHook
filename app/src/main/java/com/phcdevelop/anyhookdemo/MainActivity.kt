package com.phcdevelop.anyhookdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.fragment.app.FragmentActivity
import com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback
import com.phcdevelop.anyhookdemo.async.AsyncInstance
import com.phcdevelop.anyhookdemo.ui.theme.AnyHookDemoTheme

/*class MainActivity : FragmentActivity(),AsyncCallback{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "hook success1!!", Toast.LENGTH_LONG).show()

    }

    override fun doAsync(doOnCreate: () -> Unit) {
        AsyncInstance.instance.doAsync(doOnCreate)
    }
}*/

@Preview
@Composable
fun Greeting(@PreviewParameter(TestProvider::class) name: String) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit){
        Log.i("test", "context:$context")
    }
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
        get() = sequenceOf("test",
            "123"
        )

}
