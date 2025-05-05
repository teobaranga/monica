package com.teobaranga.monica.core.ui.pulltorefresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Stable
class MonicaPullToRefreshState(
    val onRefresh: () -> Unit,
) {

    var isRefreshing by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    val pullToRefreshState = PullToRefreshState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaPullToRefreshBox(
    state: MonicaPullToRefreshState,
    indicator: @Composable IndicatorScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = state.isRefreshing,
        onRefresh = state.onRefresh,
        state = state.pullToRefreshState,
        indicator = {
            val scope = remember {
                IndicatorScopeImpl(
                    state = state,
                    boxScope = this,
                )
            }
            with(scope) {
                indicator()
            }
        },
        content = content,
    )
}

interface IndicatorScope : BoxScope {

    val state: MonicaPullToRefreshState

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MonicaIndicator(modifier: Modifier)
}

private class IndicatorScopeImpl(
    override val state: MonicaPullToRefreshState,
    private val boxScope: BoxScope,
) : IndicatorScope, BoxScope by boxScope {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun MonicaIndicator(
        modifier: Modifier,
    ) {
        Indicator(
            modifier = modifier,
            isRefreshing = state.isRefreshing,
            state = state.pullToRefreshState,
        )
    }
}
