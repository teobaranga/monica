package com.teobaranga.monica.inject.compiler

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName

fun getFactoryTypeAlias(
    annotatedClass: KSClassDeclaration,
    parameters: Array<TypeName> = annotatedClass.getAssistedParametersTypes(),
): TypeAliasSpec {
    return TypeAliasSpec
        .builder(
            name = "Factory",
            type = LambdaTypeName.get(
                parameters = parameters,
                returnType = annotatedClass.toClassName(),
            ),
        )
        .addModifiers(KModifier.PRIVATE)
        .build()
}
