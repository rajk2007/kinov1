plugins {
    id("com.android.application") version "8.3.2"
    id("org.jetbrains.kotlin.android") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.rajk2007.kino"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rajk2007.kino"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/**"
            pickFirsts += "META-INF/kotlinx-serialization-core.kotlin_module"
            pickFirsts += "META-INF/kotlinx-serialization-json.kotlin_module"
            pickFirsts += "META-INF/library.kotlin_module"
            pickFirsts += "META-INF/okhttp.kotlin_module"
            pickFirsts += "META-INF/okio.kotlin_module"
            pickFirsts += "META-INF/kotlinx-coroutines-core.kotlin_module"
            pickFirsts += "META-INF/kotlinx-coroutines-android.kotlin_module"
            pickFirsts += "META-INF/kotlin-stdlib.kotlin_module"
            pickFirsts += "META-INF/kotlin-stdlib-jdk7.kotlin_module"
            pickFirsts += "META-INF/kotlin-stdlib-jdk8.kotlin_module"
        }
    }
}

dependencies {
    // CloudStream
    implementation("com.github.recloudstream:cloudstream:v4.6.2") {
        exclude(group = "com.github.recloudstream.cloudstream", module = "library-jvm")
    }

    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
