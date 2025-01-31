package ru.sicampus.bootcamp.domain

interface UserRepo {
    suspend fun getUsers(): Result<List<UserEntity>>
}