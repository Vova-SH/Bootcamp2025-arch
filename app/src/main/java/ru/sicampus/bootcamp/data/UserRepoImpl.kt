package ru.sicampus.bootcamp.data

import ru.sicampus.bootcamp.domain.UserEntity
import ru.sicampus.bootcamp.domain.UserRepo

class UserRepoImpl(
    private val userNetworkDataSource: UserNetworkDataSource
) : UserRepo {
    override suspend fun getUsers(
        pageNum: Int,
        pageSize: Int,
    ): Result<List<UserEntity>> {
        return userNetworkDataSource.getUsers(
            pageNum = pageNum,
            pageSize = pageSize
        ).map { pagingDto ->
            pagingDto.content?.mapNotNull { dto ->
                UserEntity(
                    id = dto.id ?: return@mapNotNull null,
                    name = dto.name ?: return@mapNotNull null,
                    email = dto.email ?: return@mapNotNull null,
                    photoUrl = dto.photoUrl ?: return@mapNotNull null
                )
            } ?: return Result.failure(IllegalStateException("List parse error"))
        }
    }
}
