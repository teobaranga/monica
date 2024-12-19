package com.teobaranga.monica

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.teobaranga.kotlininject.viewmodel.SavedStateViewModelFactory
import com.teobaranga.monica.inject.runtime.CREATION_CALLBACK_KEY
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.reflect.KClass

@Inject
@ContributesBinding(AppScope::class)
class KotlinInjectViewModelFactory(
    private val viewModelMap: Map<KClass<*>, () -> ViewModel> = emptyMap(),
    private val savedStateViewModelMap: Map<KClass<*>, (SavedStateHandle) -> ViewModel> = emptyMap(),
    private val viewModelFactoryMap: Map<KClass<*>, Any> = emptyMap(),
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return viewModelMap[modelClass]?.invoke() as? T
            ?: savedStateViewModelMap[modelClass]?.invoke(extras.createSavedStateHandle()) as? T
            ?: run {
                // { factory -> factory.create(my data here)
                val callback = extras[CREATION_CALLBACK_KEY] ?: return@run null
                val viewModelFactoryBuilder = viewModelFactoryMap[modelClass] ?: return@run null
                // the vmf is what's coming from the component
                if (viewModelFactoryBuilder is SavedStateViewModelFactory) {
                    // If the view model factory requires a saved state handle, we need to create it
                    viewModelFactoryBuilder.savedStateHandle = extras.createSavedStateHandle()
                }
                callback(viewModelFactoryBuilder) as? T
            }
            ?: error("No view model found for $modelClass")
    }
}
