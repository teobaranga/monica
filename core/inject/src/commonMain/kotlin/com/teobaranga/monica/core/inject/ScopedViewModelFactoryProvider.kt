package com.teobaranga.monica.core.inject

import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlin.reflect.KClass

interface ScopedViewModelFactoryProvider {

    fun getViewModelFactory(scope: KClass<out Any>): MetroViewModelFactory
}
