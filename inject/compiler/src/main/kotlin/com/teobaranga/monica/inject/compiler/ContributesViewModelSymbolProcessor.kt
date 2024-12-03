package com.teobaranga.monica.inject.compiler

import com.google.auto.service.AutoService
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import com.teobaranga.monica.inject.compiler.util.SavedStateHandleClassName
import com.teobaranga.monica.inject.compiler.util.asClassName
import com.teobaranga.monica.inject.runtime.ContributesViewModel

internal class ContributesViewModelSymbolProcessor(
    private val env: SymbolProcessorEnvironment,
) : SymbolProcessor {

    @AutoService(SymbolProcessorProvider::class)
    @Suppress("unused")
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            return ContributesViewModelSymbolProcessor(environment)
        }
    }

    private val savedStateViewModels = mutableListOf<KSFile>()

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation(ContributesViewModel::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { annotatedClass ->
                val fileSpec = generateComponentFile(annotatedClass)
                fileSpec
                    .writeTo(
                        codeGenerator = env.codeGenerator,
                        aggregating = false,
                        originatingKSFiles = listOf(annotatedClass.containingFile!!),
                    )
            }

        if (savedStateViewModels.isNotEmpty()) {
            generateSavedStateViewModelFactory()
                .writeTo(
                    codeGenerator = env.codeGenerator,
                    aggregating = false,
                    originatingKSFiles = savedStateViewModels,
                )
            savedStateViewModels.clear()
        }

        return emptyList()
    }

    private fun generateComponentFile(annotatedClass: KSClassDeclaration): FileSpec {
        val packageName = annotatedClass.qualifiedName?.getQualifier().orEmpty()
        val fileName = "${annotatedClass.simpleName.getShortName()}Component"
        val assistedParameters = annotatedClass.getAssistedParametersTypes()
        val factoryTypeAliasSpec = getFactoryTypeAlias(annotatedClass, assistedParameters)
        return FileSpec.builder(packageName, fileName)
            .addTypeAlias(factoryTypeAliasSpec)
            .apply {
                if (assistedParameters.any { it != SavedStateHandleClassName }) {
                    savedStateViewModels.add(annotatedClass.containingFile!!)
                    addType(
                        generateAssistedViewModelFactory(
                            annotatedClass = annotatedClass,
                            factoryTypeAlias = factoryTypeAliasSpec.asClassName(packageName),
                        ),
                    )
                }
            }
            .addType(
                generateComponentInterface(
                    annotatedClass = annotatedClass,
                    factoryTypeAlias = factoryTypeAliasSpec.asClassName(packageName),
                ),
            )
            .build()
    }
}
