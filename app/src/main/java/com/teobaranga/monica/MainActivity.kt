package com.teobaranga.monica

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import coil.ImageLoader
import com.r0adkll.kimchi.annotations.ContributesBinding
import com.r0adkll.kimchi.annotations.MergeComponent
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.teobaranga.monica.dashboard.DashboardViewModel
import com.teobaranga.monica.home.HomeViewModel
import com.teobaranga.monica.ui.datetime.LocalMonthDayFormatter
import com.teobaranga.monica.ui.datetime.getMonthDayFormatter
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.work.WorkScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.reflect.KClass

@SingleIn(AppScope::class)
@MergeComponent(AppScope::class)
abstract class AndroidAppComponent(
    /**
     * The Android application that is provided to this object graph.
     */
    @get:Provides val application: Application,
) {

    abstract val imageLoader: ImageLoader

    abstract val workScheduler: WorkScheduler

    abstract val viewModelFactory: ViewModelProvider.Factory

    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @IntoMap
    @Provides
    internal fun provideHome(homeViewModel: () -> HomeViewModel): Pair<KClass<*>, () -> ViewModel> =
        HomeViewModel::class to homeViewModel

    @IntoMap
    @Provides
    internal fun provideFoo1(dashboardViewModel: () -> DashboardViewModel): Pair<KClass<*>, () -> ViewModel> =
        DashboardViewModel::class to dashboardViewModel
}

@Inject
@ContributesBinding(AppScope::class)
class KotlinInjectViewModelFactory(private val viewModelMap: Map<KClass<*>, () -> ViewModel>): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return viewModelMap[modelClass]?.invoke() as T
    }
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                CompositionLocalProvider(
                    LocalMonthDayFormatter provides getMonthDayFormatter(),
                ) {
                    DestinationsNavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navGraph = NavGraphs.root,
                    )
                }
            }
        }
    }
}
