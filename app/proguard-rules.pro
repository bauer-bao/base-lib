# 指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
# 不要跳过混淆第三方jar成员变量
-dontskipnonpubliclibraryclassmembers
# 不要跳过第三方类
-dontskipnonpubliclibraryclasses
#不要预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保留一些属性
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod

#将api自带的设置避免混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

#忽略警告
-ignorewarnings

#如果引用了v4或者v7包、fragment
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#保持 native 的方法不去混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆，指定格式的构造方法不去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持指定规则的方法不被混淆（Android layout 布局文件中为控件配置的onClick方法不能混淆）
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持自定义控件指定规则的方法不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持枚举 enum 不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* {*;}

#保持 Parcelable 不被混淆（aidl文件不能去混淆）
-keep class * implements android.os.Parcelable {
	public static final android.os.Parcelable$Creator *;
}

#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keepnames class * implements java.io.Serializable

#保护实现接口Serializable的类中，指定规则的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

################################ 以上是app层混淆，是app的公用混淆代码 ##############################################
#-------------------------------------------- 华丽的分割线 ------------------------------------------------------#
########################################## 以下是第三方混淆代码 ###################################################

# glide 混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# 如果配置了GlideModule，需要加后面的代码  -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# fastjson 混淆
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepclassmembers class * {
    public <methods>;
}

# 友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# 对库进行混淆
-keep class com.babase.lib.** {
    public *;
}