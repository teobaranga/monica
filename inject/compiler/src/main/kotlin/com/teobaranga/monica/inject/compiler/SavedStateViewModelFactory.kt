package com.teobaranga.monica.inject.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.teobaranga.monica.inject.compiler.util.SavedStateHandleClassName

fun generateSavedStateViewModelFactory(): FileSpec {
    return FileSpec.builder("com.teobaranga.kotlininject.viewmodel", "SavedStateViewModelFactory")
        .addType(
            TypeSpec.interfaceBuilder("SavedStateViewModelFactory")
                .addProperty(
                    PropertySpec.builder("savedStateHandle", SavedStateHandleClassName)
                        .mutable(true)
                        .build()
                )
                .build()
        )
        .build()
}
