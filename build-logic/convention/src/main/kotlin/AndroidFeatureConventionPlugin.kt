import com.ohanyan.mathgame.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            dependencies {

//                add("implementation", project(":core:ui"))
//                add("implementation", project(":core:designsystem"))

//                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
//                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
//                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
//                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
//
//                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
            }
        }
    }
}
