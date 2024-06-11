plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.antsyferov.astronerd"

    compileSdk = libs.versions.compileSdk.get().toInt()


    defaultConfig {
        applicationId = "com.antsyferov.astronerd"
        versionCode = 1
        versionName = "1.0"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    /*implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:platform"))*/
    implementation(project(":data"))
    implementation(project(":data:impl"))
    //implementation(project(":features"))

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    val paging_version = "3.3.0"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:3.3.0")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)

    implementation("com.google.android.material:material:1.12.0")

    implementation(libs.androidx.core.splashscreen)

    implementation("androidx.compose.material3.adaptive:adaptive:1.0.0-beta02")
    implementation("androidx.compose.material3.adaptive:adaptive-layout:1.0.0-beta02")
    implementation("androidx.compose.material3.adaptive:adaptive-navigation:1.0.0-beta02")
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)

    implementation("io.github.sceneview:sceneview:2.2.0")

}

kapt {
    correctErrorTypes = true
}