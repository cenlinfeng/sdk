-dontoptimize
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-keepattributes *Annotation*,*JavascriptInterface*,*Annotation*,*JavascriptInterface*
-verbose
-dontwarn android.support.v4.**,android.net.compatibility.**,android.net.http.**,com.android.internal.http.multipart.**,org.apache.commons.**,org.apache.http.**
-dontwarn com.bytedance.common.**,com.bytedance.frameworks.**,com.ss.android.**,com.ddtsdk.**
-ignorewarnings

#---------------------------gson---start------------------------
#====gson====
-keep class okhttp3.**{*;}
-keep class okio.**{*;}
-keep class retrofit2.**{*;}
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
-keep class com.ddtsdk.manager.* {*;}
-keep class com.ddtsdk.listener.* {*;}
-keep class com.ddtsdk.share.**{*;}
-keep class com.ddtsdk.QuickLoginUtils{*;}
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

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.*{*;}

# 网易一键登录
-dontwarn com.cmic.sso.sdk.**
-keep public class com.cmic.sso.sdk.*{*;}
-keep class cn.com.chinatelecom.account.*{*;}
-keep class com.netease.nis.quicklogin.entity.*{*;}
-keep class com.netease.nis.quicklogin.listener.*{*;}
-keep class com.netease.nis.quicklogin.QuickLogin{
    public <methods>;
    public <fields>;
}
-keep class com.netease.nis.quicklogin.helper.UnifyUiConfig{*;}
-keep class com.netease.nis.quicklogin.helper.UnifyUiConfig$Builder{
     public <methods>;
     public <fields>;
 }
-keep class com.netease.nis.quicklogin.utils.LoginUiHelper$CustomViewListener{
     public <methods>;
     public <fields>;
}
-dontwarn com.sdk.**
-keep class com.sdk.** { *;}
-keep class com.cmic.sso.wy.** { *;}
-keep class cn.com.chinatelecom.account.** { *;}
-keep class com.netease.nis.**{*;}

#------------------------ttsdk  start---------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepparameternames
-keep class com.bytedance.** {*;}
-keep class com.bytedance.applog.AppLog { public *; }
-keep public interface com.bytedance.applog.IDataObserver { *; }
-keep public interface com.bytedance.applog.IAppParam { *; }
-keep public interface com.bytedance.applog.IExtraParams { *; }
-keep public interface com.bytedance.applog.IPicker { *; }
-keep public interface com.bytedance.applog.IOaidObserver { *; }
-keep class com.bytedance.applog.IOaidObserver$Oaid { *; }
-keep class com.bytedance.applog.GameReportHelper { public *; }
-keep class com.bytedance.applog.WhalerGameHelper { *; }
-keep class com.bytedance.applog.WhalerGameHelper$Result { *; }
-keep class com.bytedance.applog.InitConfig { public *; }
-keep class com.bytedance.applog.util.UriConfig { public *; }
-keep class com.bytedance.applog.tracker.Tracker { public *; }
-keep class com.bytedance.applog.picker.Picker { public *; }
-keep class com.bytedance.applog.tracker.WebViewJsUtil { *; }
-keep interface com.bytedance.base_bdtracker.bt { public *; }
-keep class com.bytedance.base_bdtracker.bt$a { public *; }
-keep class com.bytedance.base_bdtracker.bt$a$a { public *; }

-dontwarn androidx.annotation.Nullable
-dontwarn androidx.annotation.NonNull
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient$Info
-dontwarn androidx.appcompat.app.AlertDialog
-dontwarn androidx.appcompat.view.menu.ListMenuItemView
-dontwarn androidx.recyclerview.widget.RecyclerView
-dontwarn androidx.swiperefreshlayout.widget.SwipeRefreshLayout
-dontwarn androidx.viewpager.widget.ViewPager
-dontwarn androidx.recyclerview.widget.RecyclerView
-dontwarn androidx.annotation.RequiresApi
-dontwarn androidx.fragment.app.FragmentActivity
-dontwarn androidx.fragment.app.Fragment
-dontwarn androidx.annotation.AnyThread
-dontwarn androidx.annotation.WorkerThread

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#------------------------ttsdk  end----------------
-keep class com.kwai.** {*;}
-keep class com.ta.** {*;}
-keep class com.ut.** {*;}
-keep class com.gism.** {*;}
#-------------------------gdtsdk  start-----------------------
-dontwarn com.qq.gdt.action.**
-keep class com.qq.gdt.action.** {*;}

-keepclasseswithmembers class * {
  native <methods>;
}
#-------------------------gdtsdk  end-----------------------

#-------------------------msasdk  start-----------------------

-keep class com.bun.miitmdid.core.** {*;}

#-------------------------msasdk  end-----------------------

#-------------------------wechat  start-----------------------
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
#-------------------------wechat  end-----------------------

#---------------------------ddtsdk-----------------------------
#---------------------------start-----------------------------
-keep class com.nostra13.universalimageloader.core.**{*;}
-keep class com.nostra13.universalimageloader.cache.**{*;}
-keep class com.ddtsdk.utils.GsonUtils{*;}
-keep class com.ddtsdk.othersdk.**{*;}

-keep class android.** {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.common.** {
    <fields>;
    public protected <methods>;
}

-keep class com.ddtsdk.ui.view.FloatView {
    <fields>;
    <methods>;
}

-keep class ApiRequestListener {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.ui.web.JsInterface {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.platform.PlatformJsInterface {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.platform.PlatformWebViewActivity {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.KLSDK {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.utils.CheckSimulator {
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.utils.MainActivityUtil {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.utils.SharedPreferenceUtil{
    <fields>;
    <methods>;
}
-keep class com.ddtsdk.ui.view.LoadingDialog {
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.mylibrary.BuildConfig{
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.utils.Base64{
    <fields>;
    <methods>;
}

-keep class com.ddtsdk.ui.activity.KLSmallGameActivity{
    <fields>;
    <methods>;
}


#------------------------ddtsdk---end-----------------------------
-keepclassmembers class * extends android.os.Parcelable {
    <fields>;
    <methods>;
}

-keep interface  android.support.v4.app.** {
    <fields>;
    <methods>;
}

-keep class android.support.v4.** {
    <fields>;
    <methods>;
}

-keep class android.net.compatibility.** {
    <fields>;
    <methods>;
}

-keep class android.net.http.** {
    <fields>;
    <methods>;
}

-keep class com.android.internal.http.multipart.** {
    <fields>;
    <methods>;
}

-keep class org.apache.commons.** {
    <fields>;
    <methods>;
}

-keep class org.apache.http.** {
    <fields>;
    <methods>;
}

-keep class com.bumptech.glide.Glide {
    <fields>;
    <methods>;
}

#-------------------------------default------------------------------------

-keep public class * extends android.support.v4.**

-keep public class * extends android.app.Fragment{
<fields>;
<methods>;
}
-keep public class * extends androidx.fragment.app.Fragment{
<fields>;
<methods>;
}

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class com.android.vending.licensing.ILicensingService

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
    public void get*(...);
}

-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
    public void set*(...);
    public void get*(...);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclasseswithmembers class * {
    void onClick*(...);
}

-keepclasseswithmembers class * {
    *** *Callback(...);
}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class * extends java.io.Serializable {
    public <fields>;
    public <methods>;
}

-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

# com.news.wimd.Unkte
-keep,allowshrinking class com.news.wimd.Unkte {
    <fields>;
    <methods>;
}

-keep,allowshrinking class * extends java.io.Serializable

# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  *,*,* {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
-keep class * extends java.sql.Driver

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
-keep class * extends javax.swing.plaf.ComponentUI {
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class *,*,* {
    native <methods>;
}

# Remove - System method calls. Remove all invocations of System
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.System {
    public static long currentTimeMillis();
    static java.lang.Class getCallerClass();
    public static int identityHashCode(java.lang.Object);
    public static java.lang.SecurityManager getSecurityManager();
    public static java.util.Properties getProperties();
    public static java.lang.String getProperty(java.lang.String);
    public static java.lang.String getenv(java.lang.String);
    public static java.lang.String mapLibraryName(java.lang.String);
    public static java.lang.String getProperty(java.lang.String,java.lang.String);
}

# Remove - Math method calls. Remove all invocations of Math
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.Math {
    public static double sin(double);
    public static double cos(double);
    public static double tan(double);
    public static double asin(double);
    public static double acos(double);
    public static double atan(double);
    public static double toRadians(double);
    public static double toDegrees(double);
    public static double exp(double);
    public static double log(double);
    public static double log10(double);
    public static double sqrt(double);
    public static double cbrt(double);
    public static double IEEEremainder(double,double);
    public static double ceil(double);
    public static double floor(double);
    public static double rint(double);
    public static double atan2(double,double);
    public static double pow(double,double);
    public static int round(float);
    public static long round(double);
    public static double random();
    public static int abs(int);
    public static long abs(long);
    public static float abs(float);
    public static double abs(double);
    public static int max(int,int);
    public static long max(long,long);
    public static float max(float,float);
    public static double max(double,double);
    public static int min(int,int);
    public static long min(long,long);
    public static float min(float,float);
    public static double min(double,double);
    public static double ulp(double);
    public static float ulp(float);
    public static double signum(double);
    public static float signum(float);
    public static double sinh(double);
    public static double cosh(double);
    public static double tanh(double);
    public static double hypot(double,double);
    public static double expm1(double);
    public static double log1p(double);
}

# Remove - Number method calls. Remove all invocations of Number
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.* extends java.lang.Number {
    public static java.lang.String toString(byte);
    public static java.lang.Byte valueOf(byte);
    public static byte parseByte(java.lang.String);
    public static byte parseByte(java.lang.String,int);
    public static java.lang.Byte valueOf(java.lang.String,int);
    public static java.lang.Byte valueOf(java.lang.String);
    public static java.lang.Byte decode(java.lang.String);
    public int compareTo(java.lang.Byte);
    public static java.lang.String toString(short);
    public static short parseShort(java.lang.String);
    public static short parseShort(java.lang.String,int);
    public static java.lang.Short valueOf(java.lang.String,int);
    public static java.lang.Short valueOf(java.lang.String);
    public static java.lang.Short valueOf(short);
    public static java.lang.Short decode(java.lang.String);
    public static short reverseBytes(short);
    public int compareTo(java.lang.Short);
    public static java.lang.String toString(int,int);
    public static java.lang.String toHexString(int);
    public static java.lang.String toOctalString(int);
    public static java.lang.String toBinaryString(int);
    public static java.lang.String toString(int);
    public static int parseInt(java.lang.String,int);
    public static int parseInt(java.lang.String);
    public static java.lang.Integer valueOf(java.lang.String,int);
    public static java.lang.Integer valueOf(java.lang.String);
    public static java.lang.Integer valueOf(int);
    public static java.lang.Integer getInteger(java.lang.String);
    public static java.lang.Integer getInteger(java.lang.String,int);
    public static java.lang.Integer getInteger(java.lang.String,java.lang.Integer);
    public static java.lang.Integer decode(java.lang.String);
    public static int highestOneBit(int);
    public static int lowestOneBit(int);
    public static int numberOfLeadingZeros(int);
    public static int numberOfTrailingZeros(int);
    public static int bitCount(int);
    public static int rotateLeft(int,int);
    public static int rotateRight(int,int);
    public static int reverse(int);
    public static int signum(int);
    public static int reverseBytes(int);
    public int compareTo(java.lang.Integer);
    public static java.lang.String toString(long,int);
    public static java.lang.String toHexString(long);
    public static java.lang.String toOctalString(long);
    public static java.lang.String toBinaryString(long);
    public static java.lang.String toString(long);
    public static long parseLong(java.lang.String,int);
    public static long parseLong(java.lang.String);
    public static java.lang.Long valueOf(java.lang.String,int);
    public static java.lang.Long valueOf(java.lang.String);
    public static java.lang.Long valueOf(long);
    public static java.lang.Long decode(java.lang.String);
    public static java.lang.Long getLong(java.lang.String);
    public static java.lang.Long getLong(java.lang.String,long);
    public static java.lang.Long getLong(java.lang.String,java.lang.Long);
    public static long highestOneBit(long);
    public static long lowestOneBit(long);
    public static int numberOfLeadingZeros(long);
    public static int numberOfTrailingZeros(long);
    public static int bitCount(long);
    public static long rotateLeft(long,int);
    public static long rotateRight(long,int);
    public static long reverse(long);
    public static int signum(long);
    public static long reverseBytes(long);
    public int compareTo(java.lang.Long);
    public static java.lang.String toString(float);
    public static java.lang.String toHexString(float);
    public static java.lang.Float valueOf(java.lang.String);
    public static java.lang.Float valueOf(float);
    public static float parseFloat(java.lang.String);
    public static boolean isNaN(float);
    public static boolean isInfinite(float);
    public static int floatToIntBits(float);
    public static int floatToRawIntBits(float);
    public static float intBitsToFloat(int);
    public static int compare(float,float);
    public boolean isNaN();
    public boolean isInfinite();
    public int compareTo(java.lang.Float);
    public static java.lang.String toString(double);
    public static java.lang.String toHexString(double);
    public static java.lang.Double valueOf(java.lang.String);
    public static java.lang.Double valueOf(double);
    public static double parseDouble(java.lang.String);
    public static boolean isNaN(double);
    public static boolean isInfinite(double);
    public static long doubleToLongBits(double);
    public static long doubleToRawLongBits(double);
    public static double longBitsToDouble(long);
    public static int compare(double,double);
    public boolean isNaN();
    public boolean isInfinite();
    public int compareTo(java.lang.Double);
    public <init>(byte);
    public <init>(short);
    public <init>(int);
    public <init>(long);
    public <init>(float);
    public <init>(double);
    public <init>(java.lang.String);
    public byte byteValue();
    public short shortValue();
    public int intValue();
    public long longValue();
    public float floatValue();
    public double doubleValue();
    public int compareTo(java.lang.Object);
    public boolean equals(java.lang.Object);
    public int hashCode();
    public java.lang.String toString();
}

# Remove - String method calls. Remove all invocations of String
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.String {
    public <init>();
    public <init>(byte[]);
    public <init>(byte[],int);
    public <init>(byte[],int,int);
    public <init>(byte[],int,int,int);
    public <init>(byte[],int,int,java.lang.String);
    public <init>(byte[],java.lang.String);
    public <init>(char[]);
    public <init>(char[],int,int);
    public <init>(java.lang.String);
    public <init>(java.lang.StringBuffer);
    public static java.lang.String copyValueOf(char[]);
    public static java.lang.String copyValueOf(char[],int,int);
    public static java.lang.String valueOf(boolean);
    public static java.lang.String valueOf(char);
    public static java.lang.String valueOf(char[]);
    public static java.lang.String valueOf(char[],int,int);
    public static java.lang.String valueOf(double);
    public static java.lang.String valueOf(float);
    public static java.lang.String valueOf(int);
    public static java.lang.String valueOf(java.lang.Object);
    public static java.lang.String valueOf(long);
    public boolean contentEquals(java.lang.StringBuffer);
    public boolean endsWith(java.lang.String);
    public boolean equalsIgnoreCase(java.lang.String);
    public boolean equals(java.lang.Object);
    public boolean matches(java.lang.String);
    public boolean regionMatches(boolean,int,java.lang.String,int,int);
    public boolean regionMatches(int,java.lang.String,int,int);
    public boolean startsWith(java.lang.String);
    public boolean startsWith(java.lang.String,int);
    public byte[] getBytes();
    public byte[] getBytes(java.lang.String);
    public char charAt(int);
    public char[] toCharArray();
    public int compareToIgnoreCase(java.lang.String);
    public int compareTo(java.lang.Object);
    public int compareTo(java.lang.String);
    public int hashCode();
    public int indexOf(int);
    public int indexOf(int,int);
    public int indexOf(java.lang.String);
    public int indexOf(java.lang.String,int);
    public int lastIndexOf(int);
    public int lastIndexOf(int,int);
    public int lastIndexOf(java.lang.String);
    public int lastIndexOf(java.lang.String,int);
    public int length();
    public java.lang.CharSequence subSequence(int,int);
    public java.lang.String concat(java.lang.String);
    public java.lang.String replaceAll(java.lang.String,java.lang.String);
    public java.lang.String replace(char,char);
    public java.lang.String replaceFirst(java.lang.String,java.lang.String);
    public java.lang.String[] split(java.lang.String);
    public java.lang.String[] split(java.lang.String,int);
    public java.lang.String substring(int);
    public java.lang.String substring(int,int);
    public java.lang.String toLowerCase();
    public java.lang.String toLowerCase(java.util.Locale);
    public java.lang.String toString();
    public java.lang.String toUpperCase();
    public java.lang.String toUpperCase(java.util.Locale);
    public java.lang.String trim();
}

# Remove - StringBuffer method calls. Remove all invocations of StringBuffer
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.StringBuffer {
    public <init>();
    public <init>(int);
    public <init>(java.lang.String);
    public <init>(java.lang.CharSequence);
    public java.lang.String toString();
    public char charAt(int);
    public int capacity();
    public int codePointAt(int);
    public int codePointBefore(int);
    public int indexOf(java.lang.String,int);
    public int lastIndexOf(java.lang.String);
    public int lastIndexOf(java.lang.String,int);
    public int length();
    public java.lang.String substring(int);
    public java.lang.String substring(int,int);
}

# Remove - StringBuilder method calls. Remove all invocations of StringBuilder
# methods without side effects whose return values are not used.
-assumenosideeffects public class java.lang.StringBuilder {
    public <init>();
    public <init>(int);
    public <init>(java.lang.String);
    public <init>(java.lang.CharSequence);
    public java.lang.String toString();
    public char charAt(int);
    public int capacity();
    public int codePointAt(int);
    public int codePointBefore(int);
    public int indexOf(java.lang.String,int);
    public int lastIndexOf(java.lang.String);
    public int lastIndexOf(java.lang.String,int);
    public int length();
    public java.lang.String substring(int);
    public java.lang.String substring(int,int);
}