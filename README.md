# AnyHook
为了在compose的Preview功能时，通过LocalContext.current能获取到对应activity子类的实例。在compose的设定中，只能获取到PreviewActivity的实例，是ComponentActivity的子类。

# 使用方法
  在根目录下的build.gradle中添加
  ```gradle
  allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
  }
  ```
  在app的buidle.gradle中添加
  ```gradle
      implementation 'com.github.panghuaicheng:AnyHook:latestVersion'
  ```
  
  在application中调用
  ```kotlin
      override fun onCreate() {
        super.onCreate()
        PreviewHook.hookHandle(this,MPreviewAct::class.java)
    }
  ```
  
  在自己创建的activity的子类（不需要再manifest中注册）中调用
  ```kotlin
  class MPreviewAct: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActCreate(savedInstanceState)
    }
}
```

这样，在调用compose的preview功能时可以获取到自己的activity实例
