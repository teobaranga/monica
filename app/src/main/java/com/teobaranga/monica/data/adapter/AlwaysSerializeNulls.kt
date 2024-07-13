package com.teobaranga.monica.data.adapter

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.rawType
import java.lang.reflect.Type

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AlwaysSerializeNulls

class AlwaysSerializeNullsFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val rawType: Class<*> = type.rawType
        if (!rawType.isAnnotationPresent(AlwaysSerializeNulls::class.java)) {
            return null
        }
        val delegate: JsonAdapter<Any> = moshi.nextAdapter(this, type, annotations)
        return delegate.serializeNulls()
    }
}
