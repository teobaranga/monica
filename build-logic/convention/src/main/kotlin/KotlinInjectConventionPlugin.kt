import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.addAll
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("unused")
class KotlinInjectConventionPlugin : Plugin<Project> {

    private val Project.kotlinInjectCompilerLibs
        get() = arrayOf(
            libs.kotlin.inject.compiler,
            libs.kotlin.inject.anvil.compiler,
            libs.kotlin.inject.viewmodel.compiler,
        )

    private val androidTargetName = "android"
    private val iosTargetNames = setOf("iosX64", "iosArm64", "iosSimulatorArm64")

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.ksp.get().pluginId)

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
                    afterEvaluate {
                        findByName("androidUnitTest")?.let {
                            project.dependencies {
                                // TODO there's a bug in the ViewModel compiler, add in here when fixed
                                add("kspAndroidTest", libs.kotlin.inject.compiler)
                                add("kspAndroidTest", libs.kotlin.inject.anvil.compiler)
                            }
                        }
                    }
                }
            }
            dependencies {
                afterEvaluate {
                    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
                    project.extensions.configure<MonicaExtension> {
                        when (inject.injectIn) {
                            InjectHandler.Target.COMMON -> {
                                addAll("kspCommonMainMetadata", kotlinInjectCompilerLibs)
                            }

                            InjectHandler.Target.SEPARATE -> {
                                val targetNames = kmpExtension.targets.names
                                if (androidTargetName in targetNames) {
                                    addAll("kspAndroid", kotlinInjectCompilerLibs)
                                }
                                iosTargetNames.forEach { iosTargetName ->
                                    if (iosTargetName in targetNames) {
                                        addAll("ksp${iosTargetName.uppercaseFirstChar()}", kotlinInjectCompilerLibs)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // https://github.com/google/ksp/issues/963#issuecomment-1894144639
            // Make sure we generate code for the common source set
            afterEvaluate {
                val monica = extensions.getByType<MonicaExtension>()
                if (monica.inject.injectIn == InjectHandler.Target.COMMON) {
                    tasks.withType<KotlinCompilationTask<*>>().all {
                        if (name != "kspCommonMainKotlinMetadata") {
                            dependsOn("kspCommonMainKotlinMetadata")
                        }
                    }
                }
            }
        }
    }
}
