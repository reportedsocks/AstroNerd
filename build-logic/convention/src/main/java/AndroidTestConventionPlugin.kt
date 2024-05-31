import dev.antsyferov.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "testImplementation"(libs.findLibrary("android-test-core-ktx").get())
                "testImplementation"(libs.findLibrary("android-test-runner").get())
                "testImplementation"(libs.findLibrary("android-test-rules").get())
                "testImplementation"(libs.findLibrary("androidx-core-testing").get())
                "testImplementation"(libs.findLibrary("androidx-junit").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("junit-platform-runner").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("kotlin-test-junit").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("paging-testing").get())
            }
        }
    }
}