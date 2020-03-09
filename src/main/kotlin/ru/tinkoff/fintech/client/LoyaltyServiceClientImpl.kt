package ru.tinkoff.fintech.client

import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.LoyaltyProgram

@Service
class LoyaltyServiceClientImpl @Autowired constructor(
    private val apacheHttpClientService: ApacheHttpClientService
): LoyaltyServiceClient {

    @Value("\${paimentprocessing.loyaly.uri}")
    private val uri: String?=null

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {
        return runBlocking {
            val client = apacheHttpClientService.getHttpClient()

            val response: LoyaltyProgram = client.get {
                url(uri + id)
                contentType(ContentType.Application.Json)
            }

            client.close()

            return@runBlocking response
        }
    }
}