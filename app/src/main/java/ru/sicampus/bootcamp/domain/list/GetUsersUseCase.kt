package ru.sicampus.bootcamp.domain.list

class GetUsersUseCase(
    private val repo: UserRepo
) {
    suspend operator fun invoke() = repo.getUsers()
}
