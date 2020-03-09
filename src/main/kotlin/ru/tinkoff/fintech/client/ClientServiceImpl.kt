package ru.tinkoff.fintech.client

import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Client

@Service
class ClientServiceImpl @Autowired constructor(
    private val apacheHttpClientService: ApacheHttpClientService
): ClientService {

    @Value("\${paimentprocessing.client.uri}")
    private val uri: String?=null

    override fun getClient(id: String): Client {
        return runBlocking {
            val client = apacheHttpClientService.getHttpClient()

            val response: Client = client.get {
                url(uri + id)
                contentType(ContentType.Application.Json)
            }

            client.close()

            return@runBlocking response
        }
    }
}