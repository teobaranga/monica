package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.teobaranga.monica.destinations.SetupDestination
import com.teobaranga.monica.ui.theme.MonicaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    startRoute = SetupDestination,
                )
            }
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen() {
    // Compose Destinations needs a start route to work. In practice, the start destination is computed
    // dynamically and can be either the setup screen or the dashboard screen. This is a temporary placeholder
    // until a cleaner solution is put in place.
}
