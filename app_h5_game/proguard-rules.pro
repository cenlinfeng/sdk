# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarnings

-dontwarn com.ddtsdk.**
-keep class com.ddtsdk.**
-keep class com.nostra13.universalimageloader.core.**{*;}
-keep class com.nostra13.universalimageloader.cache.**{*;}
-keep class com.bun.miitmdid.core.** {*;}
-keep class com.qq.gdt.action.**{*;}
-keep class com.ddtsdk.othersdk.**{*;}
-keep class com.ddtsdk.common.** {
    <fields>;
    public protected <methods>;
}
-keep class com.bytedance.** {*;}
-keep class com.ddt.h5game.**{
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.utils.GsonUtils{*;}
-keep class com.ddtsdk.utils.SharedPreferenceUtil{
    <fields>;
    <methods>;
}

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep class com.ddtsdk.view.FloatView {
    <fields>;
    <methods>;
}
-keep class ApiRequestListener {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.activity.KLPaywebActivity {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.activity.KLUserinfoActivity {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.utils.JsInterface {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.model.LoginMessageinfo {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.model.PaymentInfo {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.common.KLSDK {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.activity.MHPerActivity {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.utils.Base64{
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.ui.view.LoadingDialog {
    <fields>;
    <methods>;
}


-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#---------------------------gson---start------------------------
#====gson====
-keep class sun.misc.Unsafe{*;}
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class com.google.gson.stream.**{*;}
-keep class com.google.gson.examples.android.model.**{*;}
# 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
-keepattributes EnclosingMethod
#不混淆所有的com.ddt.game.gamebox.model包下的类和这些类的所有成员变量
-keep class com.ddtsdk.common.network.** {*;}
-keep class com.ddtsdk.model.** {*;}
-keep class com.ddtsdk.manager.** {*;}
-keep class com.ddtsdk.listener.** {*;}
#---------------------------gson---end------------------------

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class okhttp3.**{ *;}
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# Okio
-dontwarn com.squareup.**
-keep class okio.**{ *;}
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
-keep class okio.**{ *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#腾讯X5浏览器内核
-dontwarn com.tencent.smtt.**
-keep class com.tencent.smtt.** {*;}
-keep class com.tencent.tbs.** {*;}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#------------------------ttsdk  start---------------

