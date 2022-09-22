package com.phcdevelop.anyhook.preview_hook.hook_interface

/**
 * @Author PHC
 * @Data 2022/5/10 18:40
 */
class HookTask(val before:()->Unit) {
    var doOnCreate:()->Unit = {}
    internal set

    var doAfter:(()->Unit)? = null
}