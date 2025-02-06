package ru.sicampus.bootcamp.domain

class GetUsersUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke(
        pageNum: Int,
        pageSize: Int,
    ) = repo.getUsers(pageNum, pageSize)
}
