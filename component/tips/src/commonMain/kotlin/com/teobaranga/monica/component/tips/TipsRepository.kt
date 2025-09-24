package com.teobaranga.monica.component.tips

import com.teobaranga.monica.component.tips.data.local.TipsDao
import com.teobaranga.monica.core.dispatcher.Dispatcher
import me.tatarka.inject.annotations.Inject

@Inject
class TipsRepository(
    private val dispatcher: Dispatcher,
    private val tipsDao: TipsDao,
) {

    suspend fun isTipSeen(id: String): Boolean {
        return with(dispatcher.default) {
            tipsDao.get(id).firstOrNull()?.isSeen ?: false
        }
    }

    suspend fun markAsSeen(id: String) {
        with(dispatcher.default) {
            tipsDao.upsert(TipEntity(id, true))
        }
    }

    suspend fun deleteAll() {
        with(dispatcher.default) {
            tipsDao.deleteAll()
        }
    }
}
