package ru.tinkoff.fintech.commons.annotation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class Utils {

    companion object Factory {
        fun getObjectMapper(): ObjectMapper {
            return ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(ISO8601DateFormat()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }

}