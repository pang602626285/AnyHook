package com.phcdevelop.anyhook.preview_hook.until

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.ComposableInvoker
import com.phcdevelop.anyhook.until.field
import com.phcdevelop.anyhook.until.method

/**
 * 基于PreviewActivity中代码，通过放射进行调用
 */
object PreviewActReflect {
    internal fun ComponentActivity.reflectActCreate() {
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == 0) {
            finish()
            return
        }

        intent?.getStringExtra("composable")?.let { setComposableContent(it) }
    }


    private fun ComponentActivity.setComposableContent(composableFqn: String) {
        val className = composableFqn.substringBeforeLast('.')
        val methodName = composableFqn.substringAfterLast('.')

        //解析Provider，兼容多预览值
        intent.getStringExtra("parameterProviderClassName")?.let { parameterProvider ->
            setParameterizedContent(className, methodName, parameterProvider)
            return@setComposableContent
        }
        //没有provider
        setContent {
            "androidx.compose.ui.tooling.CommonPreviewUtils".method("invokeComposableViaReflection")
                ?.invoke(
                    "androidx.compose.ui.tooling.CommonPreviewUtils".field("INSTANCE")
                        ?.get(null),
                    className,
                    methodName,
                    currentComposer,
                    arrayOf<Any>()
                )
            /*invokeComposableViaReflection(
                className,
                methodName,
                currentComposer
            )*/
        }
    }

    /**
     * Sets the activity content according to a given `@PreviewParameter` provider. If
     * `parameterProviderIndex` is also set, the content will be a single `@Composable` that uses
     * the `parameterProviderIndex`-th value in the provider's sequence as the argument value.
     * Otherwise, the content will display a FAB that changes the argument value on click, cycling
     * through all the values in the provider's sequence.
     */
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    private fun ComponentActivity.setParameterizedContent(
        className: String,
        methodName: String,
        parameterProvider: String
    ) {
//            Log.d(TAG, "Previewing '$methodName' with parameter provider: '$parameterProvider'")
        val asPreviewProviderClass =
            "androidx.compose.ui.tooling.PreviewUtilsKt".method("asPreviewProviderClass")
                ?.invoke(
                    null,parameterProvider,
                )
        val previewParameters =
            "androidx.compose.ui.tooling.PreviewUtilsKt".method("getPreviewProviderParameters")
                ?.invoke(
                    null,
                    asPreviewProviderClass, intent.getIntExtra("parameterProviderIndex", -1)
                ) as Array<Any?>

/*        val previewParameters = getPreviewProviderParameters(
            parameterProvider.asPreviewProviderClass(),
            intent.getIntExtra("parameterProviderIndex", -1)
        )*/

        // Handle the case where parameterProviderIndex is not provided. In this case, instead of
        // showing an arbitrary value (e.g. the first one), we display a FAB that can be used to
        // cycle through all the values.
        if (previewParameters.size > 1) {//多个预览值的展示
            setContent {
                val index = remember { mutableStateOf(0) }

                Scaffold(
                    content = {
                        kotlin.runCatching {
                        "androidx.compose.ui.tooling.CommonPreviewUtils".method("invokeComposableViaReflection")
                            ?.invoke(
                                "androidx.compose.ui.tooling.CommonPreviewUtils".field("INSTANCE")
                                    ?.get(null),
                                className,
                                methodName,
                                currentComposer,
                                arrayOf(previewParameters[index.value])
                            )
                        /*invokeComposableViaReflection(
                            className,
                            methodName,
                            currentComposer,
                            previewParameters[index.value]
                        )*/
                        }.getOrElse {
                            if (it is ClassNotFoundException){//1.2.0
                                "androidx.compose.ui.tooling.ComposableInvoker".method("invokeComposable",true)
                                    ?.invoke(
                                        "androidx.compose.ui.tooling.ComposableInvoker".field("INSTANCE")
                                            ?.get(null),
                                        className,
                                        methodName,
                                        currentComposer,
                                        arrayOf(previewParameters[index.value])
                                    )
                                /*ComposableInvoker.invokeComposable(
                                    className,
                                    methodName,
                                    currentComposer,
                                    previewParameters[index.value]
                                )*/

                            }
                        }
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text("Next") },
                            onClick = {
                                index.value = (index.value + 1) % previewParameters.size
                            }
                        )
                    }
                )
            }
        } else {
            setContent {
                "androidx.compose.ui.tooling.CommonPreviewUtils".method("invokeComposableViaReflection")
                    ?.invoke(
                        "androidx.compose.ui.tooling.CommonPreviewUtils".field("INSTANCE")
                            ?.get(null),
                        className,
                        methodName,
                        currentComposer,
                        *previewParameters
                    )
                /*  invokeComposableViaReflection(
                      className,
                      methodName,
                      currentComposer,
                      *previewParameters
                  )*/
            }
        }
    }
}