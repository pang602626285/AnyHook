package com.phcdevelop.anyhook.until


fun String.method(methodName: String) =
    Class.forName(this).declaredMethods.firstOrNull { it.name.contains(methodName) }
        ?.apply {
            this.isAccessible = true
        }

fun String.field(fieldName: String) =
    Class.forName(this).declaredFields.firstOrNull { it.name.contains(fieldName) }?.apply {
        this.isAccessible = true
    }
