package ru.sicampus.bootcamp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp.R
import ru.sicampus.bootcamp.data.auth.AuthNetworkDataSource
import ru.sicampus.bootcamp.data.auth.AuthRepoImpl
import ru.sicampus.bootcamp.data.auth.AuthStorageDataSource
import ru.sicampus.bootcamp.domain.auth.IsUserExistUseCase
import ru.sicampus.bootcamp.domain.auth.LoginUseCase
import ru.sicampus.bootcamp.domain.auth.RegisterUserUseCase
import kotlin.reflect.KClass

class AuthViewModel(
    application: Application,
    private val isUserExistUseCase: IsUserExistUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
) : AndroidViewModel(application = application) {
    private val _state = MutableStateFlow<State>(getStateShow())
    val state = _state.asStateFlow()

    private val _action = Channel<Action>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val action = _action.receiveAsFlow()

    private var isNewUser: Boolean? = null

    fun changeLogin() {
        viewModelScope.launch {
            isNewUser = null
            updateState()
        }
    }

    fun clickNext(
        login: String,
        password: String,
    ) {
        viewModelScope.launch {
            _state.emit(State.Loading)
            when (isNewUser) {
                true -> {
                    registerUserUseCase(login, password).fold(
                        onSuccess = { openList() },
                        onFailure = { error ->
                            updateState(error)
                        }
                    )
                }
                false -> {
                    loginUseCase(login, password).fold(
                        onSuccess = { openList() },
                        onFailure = { error ->
                            updateState(error)
                        }
                    )
                }
                null -> {
                    isUserExistUseCase(login).fold(
                        onSuccess = { isExist ->
                            isNewUser = isExist
                            updateState()
                        },
                        onFailure = { error ->
                            updateState(error)
                        }
                    )
                }
            }
        }
    }

    private fun openList() {
        viewModelScope.launch { _action.send(Action.GoToList) }
    }

    private suspend fun updateState(error: Throwable? = null) {
        _state.emit(getStateShow(error))
    }

    private fun getStateShow(error: Throwable? = null): State.Show {
        return State.Show(
            titleText = when (isNewUser) {
                true -> getApplication<Application>().getString(R.string.sign_up)
                false -> getApplication<Application>().getString(R.string.sign_in)
                null -> getApplication<Application>().getString(R.string.hello)
            },
            showPassword = isNewUser != null,
            buttonText = when (isNewUser) {
                true -> getApplication<Application>().getString(R.string.sign_up)
                false -> getApplication<Application>().getString(R.string.sign_in)
                null -> getApplication<Application>().getString(R.string.next)
            },
            errorText = error?.toString(),
        )
    }


    sealed interface State {
        data object Loading : State
        data class Show(
            val titleText: String,
            val showPassword: Boolean,
            val buttonText: String,
            val errorText: String?,
        ) : State
    }

    sealed interface Action {
        data object GoToList : Action
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                val authRepoImpl = AuthRepoImpl(
                    authStorageDataSource = AuthStorageDataSource,
                    authNetworkDataSource = AuthNetworkDataSource,
                )
                return AuthViewModel(
                    application = application,
                    isUserExistUseCase = IsUserExistUseCase(authRepoImpl),
                    loginUseCase = LoginUseCase(authRepoImpl),
                    registerUserUseCase = RegisterUserUseCase(authRepoImpl),
                ) as T
            }
        }
    }
}
