import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("unused")
class KotlinInjectConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.ksp.get().pluginId)

            val monicaExtension = extensions.getByType<MonicaExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    commonMain {
                        dependencies {
                            implementation(libs.kotlin.inject.runtime)
                            implementation(libs.kotlin.inject.anvil.runtime)
                            implementation(libs.kotlin.inject.anvil.runtime.optional)
                            implementation(libs.kotlin.inject.viewmodel.runtime)
                            implementation(libs.kotlin.inject.viewmodel.runtime.compose)
                        }
                    }
                }

                targets.all {
                    when (targetName) {
                        "metadata" -> {
                            kotlinInjectCompilerLibs.forEach { lib ->
                                dependencies.add(
                                    "kspCommonMainMetadata",
                                    monicaExtension.inject.injectIn.flatMap { injectTarget ->
                                        when (injectTarget) {
                                            InjectHandler.Target.COMMON -> {
                                                logger.info("kspCommonMainMetadata: ${lib.get()}")
                                                lib
                                            }

                                            InjectHandler.Target.SEPARATE -> target.provider { null }
                                        }
                                    },
                                )
                            }
                        }

                        in supportedTargets -> {
                            kotlinInjectCompilerLibs.forEach { lib ->
                                dependencies.add(
                                    "ksp${targetName.uppercaseFirstChar()}",
                                    monicaExtension.inject.injectIn.flatMap { injectTarget ->
                                        when (injectTarget) {
                                            InjectHandler.Target.COMMON -> target.provider { null }
                                            InjectHandler.Target.SEPARATE -> {
                                                logger.info("ksp${targetName.uppercaseFirstChar()}: ${lib.get()}")
                                                lib
                                            }
                                        }
                                    },
                                )
                            }
                            if (targetName == "android") {
                                // TODO there's a bug in the ViewModel compiler, add in here when fixed
                                dependencies.add("kspAndroidTest", libs.kotlin.inject.compiler)
                                dependencies.add("kspAndroidTest", libs.kotlin.inject.anvil.compiler)
                            }
                        }
                    }
                }
            }

            afterEvaluate {
                // https://github.com/google/ksp/issues/963#issuecomment-1894144639
                // Make sure we generate code for the common source set
                val monica = extensions.getByType<MonicaExtension>()
                if (monica.inject.injectIn.get() == InjectHandler.Target.COMMON) {
                    tasks.withType<KotlinCompilationTask<*>>().all {
                        if (name != "kspCommonMainKotlinMetadata") {
                            dependsOn("kspCommonMainKotlinMetadata")
                        }
                    }
                }
            }
        }
    }

    companion object {

        private val supportedTargets = setOf("android", "iosX64", "iosArm64", "iosSimulatorArm64")

        private val Project.kotlinInjectCompilerLibs
            get() = arrayOf(
                libs.kotlin.inject.compiler,
                libs.kotlin.inject.anvil.compiler,
                libs.kotlin.inject.viewmodel.compiler,
            )
    }
}
