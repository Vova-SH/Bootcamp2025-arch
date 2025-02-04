package ru.sicampus.bootcamp.domain.list

interface UserRepo {
    suspend fun getUsers(): Result<List<UserEntity>>
}
