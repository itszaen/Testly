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

# Welcome-android https://github.com/stephentuso/welcome-android
-keepclassmembers class * extends com.stephentuso.welcome.WelcomeActivity {
    public static java.lang.String welcomeKey();
}


# Android-oauth-client https://github.com/wuman/android-oauth-client
## Needed to keep generic types and @Key annotations accessed via reflection
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keepclasseswithmembers class * {
  @com.google.api.client.util.Key <fields>;
}

-keepclasseswithmembers class * {
  @com.google.api.client.util.Value <fields>;
}

-keepnames class com.google.api.client.http.HttpTransport

## Needed by google-http-client-android when linking against an older platform version
-dontwarn com.google.api.client.extensions.android.**

## Needed by google-api-client-android when linking against an older platform version
-dontwarn com.google.api.client.googleapis.extensions.android.**

## Do not obfuscate but allow shrinking of android-oauth-client
-keepnames class com.wuman.android.auth.** { *; }