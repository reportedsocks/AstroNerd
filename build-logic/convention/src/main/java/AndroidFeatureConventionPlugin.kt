import dev.antsyferov.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("antsyferov.android.library")
                apply("antsyferov.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:ui"))

                add("implementation", libs.findLibrary("koin.androidx.compose.navigation").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
            }
        }
    }
}