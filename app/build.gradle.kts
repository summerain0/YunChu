plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

val versionNamePrefix = Versions.versionName

val gitCommitCount by lazy {
    "git rev-list --count HEAD".runCommand().trim().toInt()
}

val gitCommitId by lazy {
    "git rev-parse --short HEAD".runCommand().trim()
}

val appVersionName by lazy {
    "${versionNamePrefix}.r${gitCommitCount}.${gitCommitId}"
}

val appVersionCode by lazy {
    gitCommitCount
}

fun String.runCommand(): String {
    val parts = this.split("\\s".toRegex())
    val processBuilder = ProcessBuilder(*parts.toTypedArray())
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
    processBuilder.waitFor()
    return processBuilder.inputStream.bufferedReader().readText()
}

android {
    namespace = "com.cxoip.yunchu"
    compileSdk = Versions.compileSdkVersion
    ndkVersion = Versions.ndkVersion

    signingConfigs {
        create("debugSigning") {
            storeFile = file("../debug.jks")
            storePassword = "123456"
            keyAlias = "debug"
            keyPassword = "123456"
            this.enableV1Signing = true
            this.enableV2Signing = true
            this.enableV3Signing = true
        }
        create("releaseSigning") {
            storeFile = file("../summerain0.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
            this.enableV1Signing = true
            this.enableV2Signing = true
            this.enableV3Signing = true
        }
    }

    defaultConfig {
        applicationId = "com.cxoip.yunchu"
        minSdk = Versions.minSdkVersion
        targetSdk = Versions.targetSdkVersion
        versionCode = appVersionCode
        versionName = appVersionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debugSigning")
        }
        release {
            signingConfig = signingConfigs.getByName("releaseSigning")
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtensionVersion
    }

    packagingOptions.resources.excludes.add("DebugProbesKt.bin")
    packagingOptions.resources.excludes.add("kotlin-tooling-metadata.json")
    packagingOptions.resources.excludes.add("META-INF/*.version")
}

dependencies {
    implementation(project(":setting-compose"))
    implementation(project(":yunchu-http"))
    // Kotlin
    implementation(libs.core.ktx)
    implementation(libs.kotlin.reflect)
    // AndroidX
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.splashscreen.compose)
    implementation(libs.constraintlayout.compose)
    implementation(libs.collection.ktx)
    implementation(libs.paging.compose)
    implementation(libs.material)
    implementation(libs.material3)
    // Google Accompanist
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.permissions)
    // Android Camera2
    implementation(libs.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // Sora Editor
    implementation(platform(libs.sora.editor.bom))
    implementation(libs.sora.editor)
    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.zxing.core)
    implementation(libs.xLog)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}