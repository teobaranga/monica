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

/**
 * Processor that makes each `ViewModel` annotated with [ContributesViewModel] available for injection
 * through a `ViewModelProvider.Factory`. The factory for each ViewModel is put into one of three maps keyed by the
 * ViewModel's type.
 *
 * The first map includes factories for simple ViewModels that do not have any assisted parameters. Any dependencies
 * will be provided by the dependency graph.
 *
 * The second map includes factories for ViewModels with exactly one assisted parameter of type `SavedStateHandle`.
 * While this is an assisted parameter, it doesn't require manual injection as the implementation of
 * `ViewModelProvider.Factory` can provide it automatically.
 *
 * The third map includes factories for ViewModels with one or more non-SavedStateHandle parameters. These factories
 * require consumers to provide the assisted parameters at runtime. To make things easier, a `{ViewModel}Factory` type
 * is generated that can be used to create an instance of the ViewModel given the assisted parameters.
 */
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
                // TODO validation
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

    /**
     * Generate the file containing the kotlin-inject component that contributes the ViewModel factory
     * into the right map.
     */
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
