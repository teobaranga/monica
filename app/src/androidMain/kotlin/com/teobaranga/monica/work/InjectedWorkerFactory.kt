package com.teobaranga.monica.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.teobaranga.monica.sync.SyncWorker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

/**
 * [WorkerFactory] that can create Worker instance available in the DI graph.
 */
// TODO: implement a Hilt-like approach to generating this class
@Inject
@ContributesBinding(AppScope::class)
class InjectedWorkerFactory(
    private val syncWorkerFactory: SyncWorker.Factory,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> syncWorkerFactory.create(appContext, workerParameters)
            else -> null
        }
    }
}
