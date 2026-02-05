package com.teobaranga.monica.genders.data

import com.teobaranga.monica.data.DaosComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    replaces = [DaosComponent::class],
)
class TestGendersDao : GendersDao() {

    private val genders = MutableStateFlow<MutableMap<Int, GenderEntity>>(mutableMapOf())

    override fun getGenders(): Flow<List<GenderEntity>> {
        return genders
            .mapLatest {
                it.values.toList()
            }
    }

    override suspend fun getGenderById(genderId: Int): GenderEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun getGenderByName(name: String): GenderEntity? {
        synchronized(genders) {
            return genders.value.values.firstOrNull { it.name == name }
        }
    }

    override suspend fun upsertGenders(entities: List<GenderEntity>) {
        synchronized(genders) {
            for (entity in entities) {
                genders.value[entity.genderId] = entity
            }
        }
    }
}
