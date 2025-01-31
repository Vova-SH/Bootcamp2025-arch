package ru.sicampus.bootcamp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp.data.UserNetworkDataSource
import ru.sicampus.bootcamp.data.UserRepoImpl
import ru.sicampus.bootcamp.domain.GetUsersUseCase
import ru.sicampus.bootcamp.domain.UserEntity

class ListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        updateState()
    }

    fun clickRefresh() {
        updateState()
    }

    private fun updateState() {
        viewModelScope.launch {
            _state.emit(State.Loading)
            _state.emit(
                getUsersUseCase.invoke().fold(
                    onSuccess = { data ->
                        State.Show(data)
                    },
                    onFailure = { error ->
                        State.Error(error.message.toString())
                    }
                )
            )
        }
    }

    sealed interface State {
        data object Loading : State
        data class Show(
            val items: List<UserEntity>
        ) : State

        data class Error(
            val text: String
        ) : State
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ListViewModel(
                    getUsersUseCase = GetUsersUseCase(
                        repo = UserRepoImpl(
                            userNetworkDataSource = UserNetworkDataSource()
                        )
                    )
                ) as T
            }
        }
    }
}