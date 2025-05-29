package com.teobaranga.monica.log

import com.diamondedge.logging.Logger

interface LoggerOwner {

    val loggers: Set<Logger>
}
