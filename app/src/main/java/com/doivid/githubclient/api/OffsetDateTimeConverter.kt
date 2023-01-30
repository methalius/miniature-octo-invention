package com.doivid.githubclient.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class OffsetDateTimeConverter : JsonDeserializer<OffsetDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): OffsetDateTime {
        return OffsetDateTime.parse(json.asString, ISO_OFFSET_DATE_TIME)
    }
}