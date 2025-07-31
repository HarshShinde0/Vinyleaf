# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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

# Keep Google Drive API classes
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.auth.** { *; }

# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }

# Keep Room entities
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class *

# Keep Gson classes for serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Keep audio metadata classes
-keep class org.jaudiotagger.** { *; }
