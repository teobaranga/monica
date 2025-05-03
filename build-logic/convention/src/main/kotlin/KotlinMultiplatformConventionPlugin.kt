
import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Pure Kotlin multiplatform library.
 */
@Suppress("unused") // Registered as a plugin in build.gradle.kts
class KotlinMultiplatformConventionPlugin : MonicaPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.multiplatform.get().pluginId)
            }

            configureKotlinMultiplatform()
        }
    }
}

private fun Project.configureKotlinMultiplatform() = configure<KotlinMultiplatformExtension> {

    jvm()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors: String? by project
    compilerOptions {
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs.addAll(
            "-Xcontext-receivers",
            "-opt-in=kotlin.uuid.ExperimentalUuidApi",
        )

        extensions.findByType<MonicaExtension>()?.let { monicaExtension ->
            afterEvaluate {
                if (monicaExtension.optIn.experimentalCoroutinesApi) {
                    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
                if (monicaExtension.optIn.flowPreview) {
                    freeCompilerArgs.add("-opt-in=kotlinx.coroutines.FlowPreview")
                }
            }
        }
    }
}
