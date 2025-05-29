import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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

    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    // https://github.com/google/ksp/issues/567
    // Make sure common generated code is included
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }

    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors: String? by project
    compilerOptions compilerOptions@{
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs.addAll(
            "-Xcontext-receivers",
            "-Xexpect-actual-classes",
            "-opt-in=kotlin.uuid.ExperimentalUuidApi",
        )

        afterEvaluate {
            project.extensions.configure<MonicaExtension> {
                if (optIn.experimentalCoroutinesApi) {
                    this@compilerOptions.optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
                }
                if (optIn.flowPreview) {
                    this@compilerOptions.optIn.add("kotlinx.coroutines.FlowPreview")
                }
            }
        }
    }
}
