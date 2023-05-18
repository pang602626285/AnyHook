package com.phcdevelop.base

/**
 * @Author PHC
 * @Data 2023/5/18 9:54
 */
class ReflectUtil {
    companion object{
        /**
         * 反射获取创建的预览activity路径
         */
        fun createPreActPath() = Class.forName("${Constants.CREATE_CONSTANTS_PACK}.${Constants.CREATE_CONSTANTS_NAME}")
            .getField(Constants.CREATE_FIELD_NAME)
            .get(null)?.toString()
    }
}