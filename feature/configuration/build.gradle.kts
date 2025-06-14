import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.addAll
import com.teobaranga.monica.libs

val Project.kotlinInjectCompilerLibs
    get() = arrayOf(
        libs.kotlin.inject.compiler,
        libs.kotlin.inject.anvil.compiler,
        libs.kotlin.inject.viewmodel.compiler,
    )

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    androidTarget()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))

                implementation(libs.datastore.preferences)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.configuration"
}

monica {
    inject {
        injectIn = InjectHandler.Target.SEPARATE
    }
}

// TODO something is broken in the kotlin inject plugin, probably can't be using afterEvaluate
dependencies {
    addAll("kspIosX64", kotlinInjectCompilerLibs)
    addAll("kspIosArm64", kotlinInjectCompilerLibs)
    addAll("kspIosSimulatorArm64", kotlinInjectCompilerLibs)
}
