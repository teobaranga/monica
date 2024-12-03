package com.teobaranga.monica.inject.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.teobaranga.monica.inject.compiler.util.SavedStateHandleClassName

/**
 * Generate the SavedStateViewModelFactory interface which is used to tag ViewModel factories that require a
 * SavedStateHandle. This allows a `ViewModelProvider.Factory` to know which factory requires a `SavedStateHandle`
 * and provide it automatically. Without this, users would have to manually provide one.
 */
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
