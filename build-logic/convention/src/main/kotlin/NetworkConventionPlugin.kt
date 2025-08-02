import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class NetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlinx.serialization.get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    androidMain {
                        dependencies {
                            implementation(libs.ktor.client.okhttp)
                        }
                    }
                    iosMain {
                        dependencies {
                            implementation(libs.ktor.client.darwin)
                        }
                    }
                    commonMain {
                        dependencies {
                            // Ktor for networking
                            implementation(libs.ktor.client.auth)
                            implementation(libs.ktor.client.content.negotiation)
                            implementation(libs.ktor.client.logging)
                            implementation(libs.ktor.serialization.kotlinx.json)

                            // Kotlinx Serialization for JSON serialization
                            implementation(libs.kotlinx.serialization)

                            // Sandwich for a nicer way to handle network responses
                            implementation(libs.sandwich)
                            implementation(libs.sandwich.ktor)
                        }
                    }
                    commonTest {
                        dependencies {
                            implementation(libs.ktor.client.mock)
                        }
                    }
                }
            }
        }
    }
}
