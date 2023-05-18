package com.phcdevelop.preview_hook_compiler.utils

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

/**
 * @Author PHC
 * @Data 2023/5/15 18:15
 */
object RoundEnvironmentEx {
    fun RoundEnvironment.find(a:Class< out Annotation>)= this.getElementsAnnotatedWith(a).firstOrNull()

    fun MutableSet<out TypeElement>.find(a:KClass< out Annotation>)= this.firstOrNull {
        it.qualifiedName?.toString()?.equals(a.qualifiedName) == true
    }

}