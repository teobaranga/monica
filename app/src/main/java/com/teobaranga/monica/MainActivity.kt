package com.teobaranga.monica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.teobaranga.monica.ui.theme.MonicaTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MonicaTheme(
                dynamicColor = false,
            ) {
                MonicaBackground {
                    val sheetState = rememberModalBottomSheetState(
                        initialValue = ModalBottomSheetValue.Hidden,
                        skipHalfExpanded = true,
                    )
                    val bottomSheetNavigator = remember { BottomSheetNavigator(sheetState) }
                    val engine = rememberAnimatedNavHostEngine()
                    val navController = engine.rememberNavController()
                    navController.navigatorProvider += bottomSheetNavigator

                    ModalBottomSheetLayout(
                        bottomSheetNavigator = bottomSheetNavigator,
                        sheetBackgroundColor = Color.Transparent,
                        sheetElevation = 0.dp,
                    ) {
                        DestinationsNavHost(
                            modifier = Modifier
                                .fillMaxSize(),
                            navGraph = NavGraphs.root,
                            engine = engine,
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}
