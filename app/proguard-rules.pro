-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-keep class com.cxoip.yunchu.http.model.** {*;}
-keepclassmembers class com.cxoip.yunchu.http.model.** {*;}

-keep class com.cxoip.yunchu.http.service.** {*;}
-keepclassmembers class com.cxoip.yunchu.http.service.** {*;}

-keep class retrofit2.* {*;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}