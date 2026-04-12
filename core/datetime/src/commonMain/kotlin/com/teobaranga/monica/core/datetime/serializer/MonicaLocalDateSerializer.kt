package com.teobaranga.monica.core.datetime.serializer

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant

/**
 * Some dates come down from the API with full time and time zone information, even
 * though they should represent simple local dates. Current examples are birthdays
 * or activity dates. This custom serializer strips away the time and time zone and
 * returns the intended local date.
 */
object MonicaLocalDateSerializer : KSerializer<LocalDate> {

    private val delegateSerializer = Instant.serializer()

    // Serial names of descriptors should be unique, this is why we advise including app package in the name.
    override val descriptor = SerialDescriptor(
        serialName = "com.teobaranga.monica.LocalDate",
        original = delegateSerializer.descriptor,
    )

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val data = value.atStartOfDayIn(TimeZone.UTC)
        encoder.encodeSerializableValue(delegateSerializer, data)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val instant = decoder.decodeSerializableValue(delegateSerializer)
        return instant.toLocalDateTime(TimeZone.UTC).date
    }
}
