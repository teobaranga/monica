package com.teobaranga.monica.log

import timber.log.Timber

interface TimberOwner {

    val timberTrees: Set<Timber.Tree>
}
