package ru.sicampus.bootcamp.data.auth

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp.data.Network

object AuthNetworkDataSource {

    suspend fun isUserExist(login: String): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("http://10.0.2.2:9000/api/person/username/$login")
            result.status != HttpStatusCode.OK
        }
    }

    suspend fun login(token: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("http://10.0.2.2:9000/api/person/login") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}: ${result.body<String>()}")
            }
            Unit
        }
    }

    suspend fun register(login: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val result = Network.client.post("http://10.0.2.2:9000/api/person/register") {
                    contentType(ContentType.Application.Json)
                    setBody(
                        AuthRegisterDto(
                            username = login,
                            password = password,
                            name = login,
                            email = "$login@example.com"
                        )
                    )
                }
                if (result.status != HttpStatusCode.Created) {
                    error("Status ${result.status}: ${result.body<String>()}")
                }
                Unit
            }

        }
}
