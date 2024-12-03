package com.teobaranga.monica.inject.runtime

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

val CREATION_CALLBACK_KEY = object : CreationExtras.Key<(Any) -> ViewModel> {}

/**
 * Returns a new {@code CreationExtras} with the original entries plus the passed in creation
 * callback. The callback is used by Hilt to create {@link AssistedInject}-annotated {@link
 * HiltViewModel}s.
 *
 * @param callback A creation callback that takes an assisted factory and returns a {@code
 *   ViewModel}.
 */
fun <VMF> CreationExtras.withCreationCallback(callback: (VMF) -> ViewModel): CreationExtras =
    MutableCreationExtras(this).addCreationCallback(callback)

/**
 * Returns the {@code MutableCreationExtras} with the passed in creation callback added. The
 * callback is used by Hilt to create {@link AssistedInject}-annotated {@link HiltViewModel}s.
 *
 * @param callback A creation callback that takes an assisted factory and returns a {@code
 *   ViewModel}.
 */
@Suppress("UNCHECKED_CAST")
fun <VMF> MutableCreationExtras.addCreationCallback(callback: (VMF) -> ViewModel): CreationExtras =
    this.apply {
        this[CREATION_CALLBACK_KEY] = { factory -> callback(factory as VMF) }
    }


@Composable
inline fun <reified VM : ViewModel> injectedViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
): VM {
    val viewModelFactoryOwner = LocalViewModelFactoryOwner.current
    return viewModel<VM>(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        factory = viewModelFactoryOwner.viewModelFactory,
    )
}

@Composable
inline fun <reified VM : ViewModel, reified VMF> injectedViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    noinline creationCallback: (VMF) -> VM,
): VM {
    val viewModelFactoryOwner = LocalViewModelFactoryOwner.current
    return viewModel<VM>(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        factory = viewModelFactoryOwner.viewModelFactory,
        extras = viewModelStoreOwner.run {
            if (this is HasDefaultViewModelProviderFactory) {
                this.defaultViewModelCreationExtras.withCreationCallback(creationCallback)
            } else {
                CreationExtras.Empty.withCreationCallback(creationCallback)
            }
        },
    )
}
