package com.teobaranga.monica.genders.domain

import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.genders.data.GenderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGendersUseCase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val genderRepository: GenderRepository,
) {

    suspend operator fun invoke(): Flow<List<Gender>> {
        return withContext(dispatcher.default) {
            genderRepository.genders
                .map { genders ->
                    genders.map { entity ->
                        Gender(
                            id = entity.genderId,
                            name = entity.name,
                        )
                    }
                }
        }
    }
}
