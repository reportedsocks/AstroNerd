pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AstroNerd"
include(":app")
include(":data")
include(":data:impl")/*
include(":features")
include(":core")
include(":core:ui")
include(":core:common")
include(":core:platform")*/
