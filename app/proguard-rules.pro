# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/alexsergienko/AndroidStudioProjects/astudio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

-repackageclasses ''
-allowaccessmodification
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes InnerClasses
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keep class sun.misc.Unsafe { *; }
# Remove all logs
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
}

#Crashlytics
-keep class com.crashlytics.** { *; }

#com.google
-keep class com.google.android.** {*;}
-keep class com.google.common.** { *; }
-dontwarn com.google.common.**
-dontwarn com.google.android.**

#image loader
-dontwarn com.nostra13.**
-dontwarn okio.**

#-dontwarn io.fabric.sdk.**
-dontwarn com.facebook.**

#Models
-keep class com.stockroompro.api.** { *; }
-keep class com.stockroompro.models.** { *; }


#Database
-keep class com.stockroompro.database.** { *; }

#OKHttp
-dontwarn com.squareup.okhttp.**

-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-keep class org.appache.commons.** { *; }
#imagepicker
-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

#Retrofit
-keep class retrofit.** { *; }
-keep interface retrofit.** { *; }
-keepclassmembers class retrofit.** { *; }
-keepclasseswithmembers class retrofit.** { *; }
-dontwarn retrofit.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends net.sqlcipher.database.SQLiteOpenHelper

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
native <methods>;
}

-keepclasseswithmembernames class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
