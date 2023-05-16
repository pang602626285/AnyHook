package com.phcdevelop.preview_hook_annotation

/**
 * 指定生成previewAct的父类，不指定默认为ComponentActivity
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class PreviewActParent