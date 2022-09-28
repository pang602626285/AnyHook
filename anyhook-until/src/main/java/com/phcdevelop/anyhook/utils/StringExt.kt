package com.phcdevelop.anyhook.utils


fun String.method(methodName: String,isStrictMode:Boolean = false) =
    Class.forName(this).declaredMethods.firstOrNull {
        if (isStrictMode){
            it.name.equals(methodName)
        }else {
            it.name.contains(methodName)
        }
    }
        ?.apply {
            this.isAccessible = true
        }

fun String.field(fieldName: String) =
    Class.forName(this).declaredFields.firstOrNull { it.name.contains(fieldName) }?.apply {
        this.isAccessible = true
    }
