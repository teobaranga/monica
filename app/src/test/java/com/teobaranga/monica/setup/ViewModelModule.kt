package com.teobaranga.monica.setup

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides

@Module
object ViewModelModule {

    @Provides
    fun provideSavedStateHandle() = SavedStateHandle()
}
