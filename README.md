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
  ```
  在app的build.gradle中添加
  ```gradle
      debugImplementation 'com.github.phcdevelop.anyhook:anyhook:latestVersion'
  ```
  在AndroidManifest中添加
  ```xml
          <provider
              android:authorities="com.phcdevelop.anyhook.provider.AnyProvider"
              android:name="com.phcdevelop.anyhook.provider.AnyProvider"
              android:grantUriPermissions="true"
              android:exported="false">
              <meta-data android:name="previewActName" android:value="com.phcdevelop.anyhookdemo.MainActivity"/>
          </provider>
  ```
  其中previewActName对应的value为ComponentActivity的子类（不需要在manifest中注册）的全包名。这样，在调用compose的preview功能时通过 LocalContext.current 可以获取到自己的activity实例
