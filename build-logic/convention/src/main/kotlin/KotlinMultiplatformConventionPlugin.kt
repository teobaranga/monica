import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.configureUnitTests
import com.teobaranga.monica.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Kotlin multiplatform library convention plugin, defining all the supported targets.
 */
@Suppress("unused") // Registered as a plugin in build.gradle.kts
class KotlinMultiplatformConventionPlugin : MonicaPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlin.multiplatform.get().pluginId)
                apply(libs.plugins.android.kotlin.multiplatform.library.get().pluginId)
            }

            configureKotlinMultiplatform()

            configureUnitTests()
        }
    }
}

private fun Project.configureKotlinMultiplatform() = configure<KotlinMultiplatformExtension> {

    configure<KotlinMultiplatformAndroidLibraryTarget> {
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    // https://github.com/google/ksp/issues/567
    // Make sure common generated code is included
    with(sourceSets) {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        iosArm64Main {
            kotlin.srcDir("build/generated/ksp/iosArm64/iosArm64Main/kotlin")
        }
        iosSimulatorArm64Main {
            kotlin.srcDir("build/generated/ksp/iosSimulatorArm64/iosSimulatorArm64Main/kotlin")
        }
    }

    with(sourceSets) {
        commonTest {
            dependencies {
                implementation(project(":core:test"))
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)

                implementation(libs.junit)
                implementation(libs.kotlinx.coroutines.test)

                implementation(libs.turbine)
            }
        }
        all {
            when (name) {
                "androidHostTest" -> {
                    // TODO run tests in common source set and don't forget about CI setup
                    dependencies {
                        implementation(libs.kotest.runner.junit5)
                        implementation(libs.kotest.extensions.htmlreporter)
                        implementation(libs.kotest.extensions.junitxml)
                        // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
                        implementation(libs.junit.vintage)

                        implementation(libs.mockk)
                    }
                }
            }
        }
    }

    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors: String? by project
    compilerOptions compilerOptions@{
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xexpect-actual-classes",
            "-opt-in=kotlin.uuid.ExperimentalUuidApi",
            "-opt-in=kotlin.time.ExperimentalTime",
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
