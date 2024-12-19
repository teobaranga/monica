package com.teobaranga.monica.genders.data

import com.skydoves.sandwich.ApiResponse
import com.teobaranga.monica.inject.runtime.ApplicationContext
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GenderRepository(
    @ApplicationContext
    private val appCoroutineScope: CoroutineScope,
    private val dispatcher: Dispatcher,
    private val gendersApi: GendersApi,
    private val gendersDao: GendersDao,
) {

    val genders = gendersDao.getGenders()

    suspend fun getById(genderId: Int): GenderEntity? {
        return withContext(dispatcher.io) {
            gendersDao.getGenderById(genderId)
        }
    }

    suspend fun getByName(name: String): GenderEntity? {
        return withContext(dispatcher.io) {
            gendersDao.getGenderByName(name)
        }
    }

    suspend fun fetchLatestGenders() {
        appCoroutineScope.async {
            withContext(dispatcher.io) {
                when (val genders = gendersApi.getGenders()) {
                    is ApiResponse.Success -> {
                        val entities = genders.data.data
                            .map { gender ->
                                gender.toEntity()
                            }
                        gendersDao.upsertGenders(entities)
                    }

                    else -> {
                        // TODO: Handle error
                    }
                }
            }
        }.await()
    }

    private fun GendersResponse.Gender.toEntity(): GenderEntity {
        return GenderEntity(
            genderId = id,
            name = name,
        )
    }
}
