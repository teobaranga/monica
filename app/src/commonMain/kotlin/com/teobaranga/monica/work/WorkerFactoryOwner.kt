package com.teobaranga.monica.work

expect abstract class WorkerFactory

interface WorkerFactoryOwner {

    val workerFactory: WorkerFactory

    val workScheduler: WorkScheduler
}
