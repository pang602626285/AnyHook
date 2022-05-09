package com.phcdevelop.anyhook.preview_hook.hook

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.tooling.PreviewActivity
import com.phcdevelop.anyhook.preview_hook.hook_interface.HookInterface
import com.phcdevelop.anyhook.preview_hook.provider.PreviewHookProvider
import com.phcdevelop.anyhook.preview_hook.until.PreviewActReflect.reflectActCreate

/**
 * @Author PHC
 * @Data 2022/3/3 17:55
 */
internal class PreviewHook private constructor() : HookInterface {
    companion object {
        private val TAG = PreviewHook::class.java.name


        private val NAME_PREVIEW_ACT = PreviewActivity::class.java.name
//    private val NAME_PREVIEW_ACT = "androidx.compose.ui.tooling.PreviewActivity"
        /**
         * 执行bindAPP操作，各版本一致
         */
        private const val BIND_APPLICATION = 110

        /**
         * 事务执行code为100
         */
        private const val O_LAUNCH_ACTIVITY = 100

        /**
         * 事务执行code为159
         */
        private const val Q_EXECUTE_TRANSACTION = 159

        val instance: HookInterface by lazy { PreviewHook() }
    }


    /**
     * 需要在activity 的onCreate方法中调用
     */
    override fun onActCreate(activity: ComponentActivity) {
        activity.reflectActCreate()
        PreviewHookProvider.PREVIEW_ACT_NAME
//        val intent = Intent(activity.intent)
//        intent.component = ComponentName(activity,PreviewActivity::class.java)
//        activity.startActivity(intent)
    }

    /**
     * @param app App实例
     * @param replaceActClaz 需要替换启动的activity类
     */
    @SuppressLint("SoonBlockedPrivateApi", "PrivateApi", "DiscouragedPrivateApi")
    override fun init(app: Application, replaceActClaz: Class<out ComponentActivity>) {
        if (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == 0) {
            Log.i(TAG, "Application is not debuggable. Don't need hook!")
            return
        }
        val handler = getThreadHandle()

        Handler::class.java.getDeclaredField("mCallback").apply { this.isAccessible = true }
            //替换掉原来的Callback
            .set(handler, object : Handler.Callback {
                override fun handleMessage(it: Message): Boolean {
                    try {
                        when (it.what) {
                            O_LAUNCH_ACTIVITY -> {
                                handleO(app, it, replaceActClaz)
                            }
                            Q_EXECUTE_TRANSACTION -> {
                                handleQ(app, it, replaceActClaz)
                            }

                        }
                        //用系统的执行
                        handler.handleMessage(it)

                        if(BIND_APPLICATION == it.what) {
                            Handler::class.java.getDeclaredField("mCallback")
                                .apply { this.isAccessible = true }
                                .takeIf { it.get(handler)?.equals(this) != true }//不相等说明callback被替换，再次替换回来
                                ?.let {
                                    Log.i(TAG,"Handler's mCallback replace success!")
                                    it.set(handler, this)
                                }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }

            })
    }

    private fun getThreadHandle(): Handler {
        //在startAct调用过程中，Uri后通过验证后，返回App，经过ActivityThread中的Handle启动Act
        //在一个app中，只有一个ActivityThread，同样的也只有一个Handle
        val clzActivityThread = Class.forName("android.app.ActivityThread")
        //获取actThread实例
        val actThreadInstance =
            clzActivityThread.getDeclaredField("sCurrentActivityThread")
                .apply { this.isAccessible = true }.get(null)
        //获取handler
        val handler =
            clzActivityThread.getDeclaredField("mH").apply { this.isAccessible = true }
                .get(actThreadInstance) as Handler
        return handler;
    }

    /**
     * android O(8.1 27 )及之前的执行策略
     */
    private fun handleO(
        context: Context,
        msg: Message,
        replaceActClaz: Class<out ComponentActivity>
    ) {
        val intent = msg.obj.javaClass.getDeclaredField("intent").let {
            it.isAccessible = true
            it.get(msg.obj)
        } as Intent
        if (intent.component?.className == NAME_PREVIEW_ACT) {
            intent.component = ComponentName(context, replaceActClaz)
        }
    }

    /**
     * android Q(10.0 29)及之后改变了执行策略
     */
    private fun handleQ(
        context: Context,
        it: Message,
        replaceActClaz: Class<out ComponentActivity>
    ) {

        val transaction = it.obj
        //获取activityCallbacks
        val actCBList =
            Class.forName("android.app.servertransaction.ClientTransaction")
                .getDeclaredField("mActivityCallbacks")
                .apply { this.isAccessible = true }
                .get(transaction) as List<Any>
        listOf(2).firstOrNull()
        //因为马上要执行，一般在第0个
        if (actCBList.isNotEmpty()) {
            actCBList[0].takeIf { it.javaClass.name.equals("android.app.servertransaction.LaunchActivityItem") }
                ?.let { launchActItem ->
                    //启动新的act时，会从LaunchActivityItem读取Intent，所以要替换掉这个
                    Class.forName("android.app.servertransaction.LaunchActivityItem")
                        .getDeclaredField("mIntent")
                        .apply { this.isAccessible = true }
                        .let { intentField ->
                            /*val rawIntent =
                                (intentField.get(launchActItem) as? Intent)?.getParcelableExtra<Intent>(
                                    TARGET_INTENT
                                )
                            intentField.set(launchActItem, rawIntent)*/
                            (intentField.get(launchActItem) as? Intent)?.takeIf { intent ->
                                intent.component?.className == NAME_PREVIEW_ACT
                            }?.apply {
                                this.component = ComponentName(
                                    context,
                                    replaceActClaz
                                )
                            }
                        }

                }
        }
    }

}