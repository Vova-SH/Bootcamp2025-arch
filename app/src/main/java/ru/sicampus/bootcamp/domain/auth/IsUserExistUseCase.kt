package ru.sicampus.bootcamp.domain.auth

class IsUserExistUseCase(
    private val authRepo: AuthRepo,
) {
    suspend operator fun invoke(login: String): Result<Boolean> {
        return authRepo.isUserExist(login)
    }
}
