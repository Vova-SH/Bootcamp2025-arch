package ru.sicampus.bootcamp.domain.auth

class RegisterUserUseCase(
    private val authRepo: AuthRepo,
) {
    suspend operator fun invoke(login: String, password: String): Result<Unit> {
        return authRepo.register(login, password).mapCatching {
            authRepo.login(login, password)
        }
    }
}
