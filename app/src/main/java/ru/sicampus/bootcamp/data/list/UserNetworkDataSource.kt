package ru.sicampus.bootcamp.data.list

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sicampus.bootcamp.data.Network

class UserNetworkDataSource {
    suspend fun getUsers(
        token: String
    ): Result<List<UserDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val result = Network.client.get("http://10.0.2.2:9000/api/person") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }
            if (result.status != HttpStatusCode.OK) {
                error("Status ${result.status}")
            }
            result.body()
        }
    }
}
