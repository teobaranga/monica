package com.teobaranga.monica.activity.edit.domain

import com.teobaranga.monica.activity.data.ContactActivitiesRepository
import com.teobaranga.monica.activity.edit.ui.MapContactToActivityParticipant
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetActivityUseCase(
    private val dispatcher: Dispatcher,
    private val contactActivitiesRepository: ContactActivitiesRepository,
    private val mapContactToActivityParticipant: MapContactToActivityParticipant,
) {

    suspend operator fun invoke(activityId: Int): Activity {
        return withContext(dispatcher.io) {
            val activity = contactActivitiesRepository.getActivity(activityId).first()
            Activity(
                summary = activity.activity.title,
                details = activity.activity.description,
                date = activity.activity.date,
                participants = activity.participants.map { contact ->
                    mapContactToActivityParticipant(contact)
                },
            )
        }
    }
}
