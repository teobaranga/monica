package com.teobaranga.monica.inject.compiler

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.teobaranga.monica.inject.compiler.util.SavedStateHandleClassName
import com.teobaranga.monica.inject.compiler.util.ViewModelClassName
import com.teobaranga.monica.inject.runtime.ContributesViewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
fun KSClassDeclaration.getAssistedParameters(): List<KSValueParameter> {
    return (primaryConstructor?.parameters ?: emptyList())
        .filter { parameter ->
            parameter.getAnnotationsByType(Assisted::class).count() > 0
        }
}

fun KSClassDeclaration.getAssistedParametersTypes(): Array<TypeName> {
    return getAssistedParameters()
        .map { parameter ->
            parameter.type.toTypeName()
        }
        .toTypedArray()
}

/**
 * Generate the provide function that binds the ViewModel class to its factory.
 *
 * The factory can be either
 * - a simple lambda that returns the ViewModel in case there are no assisted parameters
 * - a lambda that takes in a `SavedStateHandle` and returns a ViewModel
 * - `Any` which represents the factory of a more complex ViewModel that has at least one non-SavedStateHandle parameter
 */
fun generateProviderFunction(annotatedClass: KSClassDeclaration, factoryTypeAlias: TypeName): FunSpec {
    val viewModelParameters = annotatedClass.getAssistedParametersTypes()

    val viewModelClassName = annotatedClass.simpleName.getShortName()

    val packageName = annotatedClass.qualifiedName?.getQualifier().orEmpty()
    val parameter = ParameterSpec
        .builder(
            name = "factory",
            type = when {
                viewModelParameters.any { it != SavedStateHandleClassName } -> {
                    ClassName.bestGuess("$packageName.${viewModelClassName}Factory")
                }
                else -> factoryTypeAlias
            },
        )
        .build()

    val returnType =
        // Pair<KClass<*>, () -> ViewModel> or
        // Pair<KClass<*>, (SavedStateHandle) -> ViewModel> or
        // Pair<KClass<*>, Any>
        Pair::class.asClassName().parameterizedBy(
            KClass::class.asClassName().parameterizedBy(STAR),
            when {
                viewModelParameters.any {
                    it != SavedStateHandleClassName
                } -> {
                    Any::class.asTypeName()
                }
                else -> LambdaTypeName.get(
                    parameters = viewModelParameters,
                    returnType = ViewModelClassName,
                )
            },
        )

    return FunSpec.builder("provide${viewModelClassName}")
        .addAnnotation(Provides::class)
        .addAnnotation(IntoMap::class)
        .addParameter(parameter)
        .returns(returnType)
        .addCode("return %T::class to %N", annotatedClass.toClassName(), parameter)
        .build()
}

@OptIn(KspExperimental::class)
fun getScope(element: KSClassDeclaration): KClass<*> {
    return element.getAnnotationsByType(ContributesViewModel::class)
        .first()
        .scope
}
