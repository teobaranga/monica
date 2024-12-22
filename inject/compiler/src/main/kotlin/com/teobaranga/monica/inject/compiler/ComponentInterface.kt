package com.teobaranga.monica.inject.compiler

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

/**
 * Generate the kotlin-inject component interface that uses anvil to contribute the ViewModel factory to the
 * right map.
 */
fun generateComponentInterface(annotatedClass: KSClassDeclaration, factoryTypeAlias: TypeName): TypeSpec {
    val interfaceName = "${annotatedClass.simpleName.getShortName()}Component"
    return TypeSpec.interfaceBuilder(interfaceName)
        .addAnnotation(
            AnnotationSpec.builder(ContributesTo::class)
                .addMember("%T::class", getScope(annotatedClass))
                .build(),
        )
        .addFunction(generateProviderFunction(annotatedClass, factoryTypeAlias))
        .build()
}
