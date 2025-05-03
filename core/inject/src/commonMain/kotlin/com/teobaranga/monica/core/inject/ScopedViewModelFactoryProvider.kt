package com.teobaranga.monica.core.inject

import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import kotlin.reflect.KClass

interface ScopedViewModelFactoryProvider {

    fun getViewModelFactoryOwner(scope: KClass<out Any>): ViewModelFactoryOwner
}
