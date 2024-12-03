package com.teobaranga.monica.work

import androidx.work.WorkerFactory

interface WorkerFactoryOwner {

    val workerFactory: WorkerFactory

    val workScheduler: WorkScheduler
}
