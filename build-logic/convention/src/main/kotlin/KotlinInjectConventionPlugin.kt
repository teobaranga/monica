import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.MonicaExtension
import com.teobaranga.monica.addAll
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
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

    private fun KotlinMultiplatformExtension.configureCommonMainKsp() {
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
            if (name != "kspCommonMainKotlinMetadata") {
                dependsOn("kspCommonMainKotlinMetadata")
            }
        }
    }

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

                afterEvaluate {
                    project.extensions.configure<MonicaExtension> {
                        val isCommonInjection = inject.injectIn == InjectHandler.Target.COMMON
                        if (isCommonInjection) {
                            configureCommonMainKsp()
                        }
                    }
                }
            }
            dependencies {
                afterEvaluate {
                    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
                    project.extensions.configure<MonicaExtension> {
                        val isCommonInjection = inject.injectIn == InjectHandler.Target.COMMON
                        if (isCommonInjection) {
                            addAll("kspCommonMainMetadata", kotlinInjectCompilerLibs)
                        } else if (kmpExtension.sourceSets.findByName("androidMain") != null) {
                            addAll("kspAndroid", kotlinInjectCompilerLibs)
                        }
                    }
                }
            }
        }
    }
}
