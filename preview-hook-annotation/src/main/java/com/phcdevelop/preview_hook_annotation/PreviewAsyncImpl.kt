package com.phcdevelop.preview_hook_annotation

/**
 * 修饰异步回调实现类
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class PreviewAsyncImplClazz

/**
 * 修饰异步回调实现属性的get方法
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class PreviewAsyncImplGetter
