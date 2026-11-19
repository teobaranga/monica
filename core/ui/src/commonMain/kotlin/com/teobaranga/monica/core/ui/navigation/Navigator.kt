package com.teobaranga.monica.core.ui.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events for the currently active back stack.
 */
interface Navigator {

    /**
     * Navigates to [route]. Top level routes switch to their own back stack instead of being
     * pushed on top of the currently active one.
     */
    fun navigate(route: NavKey)

    /**
     * Pops the currently active back stack. When the top level route is already at the base of its
     * stack, returns to the start route instead.
     */
    fun popBackStack()
}
