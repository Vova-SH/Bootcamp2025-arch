package ru.sicampus.bootcamp.domain

interface UserRepo {
    suspend fun getUsers(
        pageNum: Int,
        pageSize: Int,
    ): Result<List<UserEntity>>
}
