package ru.tinkoff.fintech.client

import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NotificationServiceClientImpl @Autowired constructor(
    private val apacheHttpClientService: ApacheHttpClientService
) : NotificationServiceClient {
    @Value("\${paimentprocessing.notification.uri}")
    private val uri: String? = null

    override fun sendNotification(clientId: String, message: String) {
        return runBlocking {
            val client = apacheHttpClientService.getHttpClient()

            client.post<String> {
                url("$uri$clientId/message")
                body = message
                contentType(ContentType.Application.Json)
            }

            client.close()

            return@runBlocking
        }

    }
}