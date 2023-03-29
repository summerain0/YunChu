plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

val versionNamePrefix = "1.0.0"

val gitCommitCount by lazy {
    "git rev-list --count HEAD".runCommand().trim().toInt()
}

val gitCommitId by lazy {
    "git rev-parse --short HEAD".runCommand().trim()
}

val appVersionName by lazy {
    "${versionNamePrefix}.${gitCommitId}"
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
    compileSdk = 33

    signingConfigs {
        create("general") {
            storeFile = file("../debug.jks")
            storePassword = "123456"
            keyAlias = "debug"
            keyPassword = "123456"
            this.enableV1Signing = true
            this.enableV2Signing = true
        }
    }

    defaultConfig {
        applicationId = "com.cxoip.yunchu"
        minSdk = 21
        targetSdk = 33
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("general")
        }
        release {
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("general")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            delete("build/outputs/profiling/baseline.prof")
            delete("build/outputs/profiling/baseline.profm")
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
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    packagingOptions.resources.excludes.add("DebugProbesKt.bin")
    packagingOptions.resources.excludes.add("kotlin-tooling-metadata.json")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.navigation.animation)

    implementation(platform(libs.sora.editor.bom))
    implementation(libs.sora.editor)

    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}