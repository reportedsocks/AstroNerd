

plugins {
    alias(libs.plugins.antsyferov.android.library.compose)
}

android {
    namespace = "com.antsyferov.features"
    compileSdk = libs.versions.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
}

