package com.teobaranga.monica.component.tips

import com.teobaranga.monica.component.tips.data.local.TipsDao
import com.teobaranga.monica.component.tips.di.TipsComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    replaces = [TipsComponent::class],
)
class TestTipsDao: TipsDao() {

    private val tips = MutableStateFlow<MutableMap<String, TipEntity>>(mutableMapOf())

    override suspend fun get(id: String): List<TipEntity> {
        return listOfNotNull(tips.value[id])
    }

    override suspend fun upsert(entity: TipEntity) {
        tips.update {
            it[entity.id] = entity
            it
        }
    }

    override suspend fun deleteAll() {
        tips.value = mutableMapOf()
    }
}
