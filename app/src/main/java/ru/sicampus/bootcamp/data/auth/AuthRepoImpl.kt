package ru.sicampus.bootcamp.data.auth

import ru.sicampus.bootcamp.domain.auth.AuthRepo

class AuthRepoImpl(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authStorageDataSource: AuthStorageDataSource,
) : AuthRepo {
    override suspend fun isUserExist(login: String): Result<Boolean> {
        return authNetworkDataSource.isUserExist(login)
    }

    override suspend fun register(login: String, password: String): Result<Unit> {
        return authNetworkDataSource.register(login, password)
    }

    override suspend fun login(login: String, password: String): Result<Unit> {
        val token = authStorageDataSource.updateToken(login, password)
        return authNetworkDataSource.login(token).onFailure {
            authStorageDataSource.clear()
        }
    }
}
