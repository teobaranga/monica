package com.teobaranga.monica.configuration.presentation

import android.content.Context
import android.content.Intent

internal fun restartApp(context: Context) {
    val packageManager = context.packageManager
    val intent = packageManager.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val mainIntent = Intent.makeRestartActivityTask(componentName)
    // Required for API 34 and later
    // Ref: https://developer.android.com/about/versions/14/behavior-changes-14#safer-intents
    mainIntent.setPackage(context.packageName)
    context.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}
