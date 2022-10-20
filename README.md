# AnyHook 
## 前言
在compose的设定中，只能获取到PreviewActivity的实例，其继承于ComponentActivity。写了这个库，为了在compose的Preview功能时，通过LocalContext.current能获取到对应activity子类的实例。主要是用了动态代理和反射，在ActivityThread最后启动activity时，把要启动的PreviewActivity信息换成我们自己的。

## 使用方法
  在根目录下的build.gradle中添加
  ```gradle
  allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
  }
  
  buildscript {
    ext {
        compose_version = '1.2.0'//对应的compose版本号
        ...
    }
}
  ```
## PreviewHook
  在app的build.gradle中添加
  ```gradle
  android{
     defaultConfig {
        ...
        manifestPlaceholders =[
                PREVIEW_HOOK_PROVIDER_AUTHORITIES:"com.phc.anyhookdemo.PreviewHookProvider",//给provider提供的authorities,随便填，保证不重复就行
                PREVIEW_HOOK_ACT_NAME:"com.phcdevelop.anyhookdemo.MainActivity",//填写需要替换成的activity，需要是ComponentActivity的子类（不需要在manifest中注册）的全包名
                COMPOSE_VERSION:"$compose_version",//传入compose版本号
        ]
    }
  }
    ...
  dependencies {
    debugImplementation 'com.github.phcdevelop.anyhook:anyhook:latestVersion'
  }
  ```

这样，在调用compose的preview功能时通过 LocalContext.current 可以获取到自己的activity实例

## PreviewHookCheck
如果你的app使用了其他框架，也使用了替换ActivityThread.Handler.Callback的方式，导致PreviewHook无效，那么你就需要使用这个库
添加依赖
```gradle
        implementation "com.github.phcdevelop.anyhook:preview-hook-check:$anyhookVersion"
```
之后，在app初始化调用
```kotlin
class MApp: Application() {
    override fun onCreate() {
        super.onCreate()
        ...
        PreviewHookCheck.checkOnAppOnCreate(this)
    }
}
```

## PreviewHookCallback
增加了异步回调的支持，如果你需要在composeAct执行前需要初始化app某些配置，可以使用

```kotlin
class MainActivity : FragmentActivity(),ASyncCallback {

    override fun doAsync(doOnCreate: () -> Unit) {
        thread {
            //业务逻辑执行
            ...
            
            doMain{//必须切换到主线程执行
                doOnCreate()
            }
        }
    }
}
```
