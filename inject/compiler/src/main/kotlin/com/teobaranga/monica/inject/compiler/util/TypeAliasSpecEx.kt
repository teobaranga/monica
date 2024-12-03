package com.teobaranga.monica.inject.compiler.util

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeAliasSpec

fun TypeAliasSpec.asClassName(packageName: String): ClassName {
    return ClassName(packageName, name)
}
