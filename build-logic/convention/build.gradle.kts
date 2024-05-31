import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "dev.antsyferov.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation.set(true)
        failOnWarning.set(true)
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "antsyferov.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "antsyferov.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "antsyferov.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "antsyferov.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        /*register("androidFeature") {
            id = "antsyferov.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }*/
        register("androidRoom") {
            id = "antsyferov.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidHilt") {
            id = "antsyferov.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidTest") {
            id = "antsyferov.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
    }
}