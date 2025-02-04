package ru.sicampus.bootcamp.data.auth

import okhttp3.Credentials

object AuthStorageDataSource {
    var token: String? = null
        private set

    fun updateToken(login: String, password: String): String {
        val updateToken = Credentials.basic(login, password)
        token = updateToken
        return updateToken
    }

    fun clear() {
        token = null
    }
}
