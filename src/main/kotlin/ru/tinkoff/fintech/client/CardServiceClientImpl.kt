package ru.tinkoff.fintech.client

import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Card

@Service
class CardServiceClientImpl @Autowired constructor(
    private val apacheHttpClientService: ApacheHttpClientService
) : CardServiceClient {

    @Value("\${paimentprocessing.card.uri}")
    private val uri: String? = null

    override fun getCard(id: String): Card {

        return runBlocking {
            val client = apacheHttpClientService.getHttpClient()

            val response: Card = client.get {
                url(uri + id)
                contentType(ContentType.Application.Json)
            }

            client.close()

            return@runBlocking response
        }
    }

}
