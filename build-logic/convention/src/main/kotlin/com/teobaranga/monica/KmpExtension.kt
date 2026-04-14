package com.teobaranga.monica

import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

/**
 * Opt-in to `kotlinx.coroutines.ExperimentalCoroutinesApi`.
 */
fun KotlinCommonCompilerOptions.optInExperimentalCoroutinesApi() {
    optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
}

/**
 * Opt-in to `kotlinx.coroutines.FlowPreview`.
 */
fun KotlinCommonCompilerOptions.optInFlowPreview() {
    optIn.add("kotlinx.coroutines.FlowPreview")
}
