package com.teobaranga.monica.contact.detail.section.activity

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.injectedViewModel
import com.teobaranga.monica.activity.nav.ContactActivityEditRoute
import com.teobaranga.monica.core.ui.Zero
import com.teobaranga.monica.core.ui.navigation.LocalNavigator

@Composable
fun ActivityListScreen(
    contactId: Int,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current
    val viewModel = injectedViewModel<ContactActivitiesViewModel, ContactActivitiesViewModel.Factory>(
        creationCallback = { factory ->
            factory(contactId)
        },
    )
    val activitiesUiState by viewModel.contactActivities.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (activitiesUiState != ContactActivitiesUiState.Loading) {
                FloatingActionButton(
                    modifier = Modifier
                        .navigationBarsPadding(),
                    onClick = {
                        navigator.navigate(ContactActivityEditRoute(contactId = contactId, activityId = null))
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add activity",
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.Zero,
    ) { contentPadding ->
        Crossfade(
            targetState = activitiesUiState::class,
            label = "Contact Activities",
        ) { uiStateClass ->
            when (uiStateClass) {
                ContactActivitiesUiState.Loading::class -> {
                    ContactActivityPlaceholder()
                }

                ContactActivitiesUiState.Empty::class -> {
                    ContactActivityEmpty(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(top = 60.dp),
                    )
                }

                ContactActivitiesUiState.Loaded::class -> {
                    ContactActivitiesColumn(
                        modifier = Modifier
                            .padding(contentPadding),
                        uiState = activitiesUiState as ContactActivitiesUiState.Loaded,
                        onActivityClick = { activityId ->
                            navigator.navigate(
                                ContactActivityEditRoute(
                                    contactId = contactId,
                                    activityId = activityId,
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}
