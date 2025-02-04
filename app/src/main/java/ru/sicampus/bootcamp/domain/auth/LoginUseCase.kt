package ru.sicampus.bootcamp.domain.auth

class LoginUseCase(
    private val authRepo: AuthRepo,
) {
    suspend operator fun invoke(login: String, password: String): Result<Unit> {
        return authRepo.login(login, password)
    }
}
