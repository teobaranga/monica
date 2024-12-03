package com.teobaranga.monica.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.teobaranga.monica.sync.SyncWorker
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class InjectedWorkerFactory internal constructor(
    private val syncWorkerFactory: (Context, WorkerParameters) -> SyncWorker,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> syncWorkerFactory(appContext, workerParameters)
            else -> null
        }
    }
}
