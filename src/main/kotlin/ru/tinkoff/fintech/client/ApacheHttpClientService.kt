package ru.tinkoff.fintech.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ApacheHttpClientService @Autowired constructor(
    private val objectMapper: ObjectMapper
) {

    fun getHttpClient(): HttpClient {

        return HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer(objectMapper) {
                }
            }
        }
    }

}