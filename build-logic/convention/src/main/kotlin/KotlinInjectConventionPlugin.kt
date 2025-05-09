import com.teobaranga.monica.implementation
import com.teobaranga.monica.ksp
import com.teobaranga.monica.kspTest
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("unused")
class KotlinInjectConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.ksp.get().pluginId)
            extensions.findByType<KotlinMultiplatformExtension>()?.run {
                sourceSets.commonMain.dependencies {
                    implementation(libs.kotlin.inject.runtime)
                    implementation(libs.kotlin.inject.anvil.runtime)
                    implementation(libs.kotlin.inject.anvil.runtime.optional)
                    implementation(libs.kotlin.inject.viewmodel.runtime)
                    implementation(libs.kotlin.inject.viewmodel.runtime.compose)
//                    kspTest(libs.kotlin.inject.compiler)
//                    kspTest(libs.kotlin.inject.anvil.compiler)
                }
                project.dependencies {
                    kspCommonMainMetadata(libs.kotlin.inject.compiler)
                    kspCommonMainMetadata(libs.kotlin.inject.anvil.compiler)
                    kspCommonMainMetadata(libs.kotlin.inject.viewmodel.compiler)
                }

                configureCommonMainKsp()
            } ?: dependencies {
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.inject.anvil.runtime)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
                implementation(libs.kotlin.inject.viewmodel.runtime)
                implementation(libs.kotlin.inject.viewmodel.runtime.compose)
                ksp(libs.kotlin.inject.compiler)
                ksp(libs.kotlin.inject.anvil.compiler)
                ksp(libs.kotlin.inject.viewmodel.compiler)
                kspTest(libs.kotlin.inject.compiler)
                kspTest(libs.kotlin.inject.anvil.compiler)
            }
        }
    }

    private fun DependencyHandler.kspCommonMainMetadata(dependencyNotation: Any) {
        add("kspCommonMainMetadata", dependencyNotation)
    }
}

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
