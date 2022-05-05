package com.phcdevelop.anyhook.hook

object ActHook {

    /**
     * 保存原始Intent
     */
    const val TARGET_INTENT = "TARGET_INTENT"

    /**
     * 是否启动hook
     */
    const val NEED_HOOK = "NEED_HOOK"

    /*@SuppressLint("BlockedPrivateApi", "PrivateApi", "DiscouragedPrivateApi")
    fun hookIActivityTaskManager(context: Context, replaceActClaz: Class<out ComponentActivity>) {
        val iSingletonField = Class.forName("android.app.ActivityTaskManager")
            .getDeclaredField("IActivityTaskManagerSingleton")
        //获取到单例模式实例
        val iActivityTaskManagerSingleton =
            iSingletonField.apply { this.isAccessible = true }.get(null)

        //拿到单例模式中的mInstance
        //内部类继承，所以要用superclass
        val instanceField =
            iActivityTaskManagerSingleton.javaClass.superclass.getDeclaredField("mInstance")
        instanceField.isAccessible = true
        *//*val instance =
            instanceField.get(iActivityTaskManagerSingleton)*//*

        //直接执行Singleton的get方法，使用里面的静态对象在系统没有调用时是null,
        val getMethod = Class.forName("android.util.Singleton").getDeclaredMethod("get")
        getMethod.isAccessible = true
        val instance = getMethod.invoke(iActivityTaskManagerSingleton)

        //使用动态代理
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(Class.forName("android.app.IActivityTaskManager"))
        ) { proxy, method, args ->
            //ActivityTaskManager.getService().startActivity
            try {
                Log.i("phc", "method:${method.name}")
                method?.takeIf { it.name.equals("startActivity") }//hook到startActivity
                    ?.let { args.indexOfFirst { it is Intent } }
*//*                    ?.takeIf {
                        it > 0 && (args[it] as? Intent)?.getBooleanExtra(
                            NEED_HOOK,
                            false
                        ) == true
                    }//只拦截需要Hook的*//*
                    ?.takeIf { it > 0 && (args[it] as? Intent)?.component?.className == PreviewHook.NAME_PREVIEW_ACT }
                    ?.let { index ->
                        //否则尝试替换Intent
                        (args[index] as? Intent)?.let { rawIntent ->
                            //替换掉Intent信息
                            rawIntent.component =
                                ComponentName(context, replaceActClaz)
                        }
                    }
                //用系统的执行
                method?.invoke(instance, *args)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //把代理替换到ActivityTaskManager中
        instanceField.set(iActivityTaskManagerSingleton, proxy)
    }*/

}