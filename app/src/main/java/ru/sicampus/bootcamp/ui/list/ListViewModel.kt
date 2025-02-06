package ru.sicampus.bootcamp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
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
    val listState = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            maxSize = 30
        )
    ) {
        ListPagingSource(getUsersUseCase::invoke)
    }.flow
        .cachedIn(viewModelScope)

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
