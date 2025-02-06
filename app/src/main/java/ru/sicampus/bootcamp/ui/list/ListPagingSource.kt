package ru.sicampus.bootcamp.ui.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.sicampus.bootcamp.domain.UserEntity

class ListPagingSource(
    private val request: suspend (pageNum: Int, pageSize: Int) -> Result<List<UserEntity>>
) : PagingSource<Int, UserEntity>()  {
    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        val pageNum = params.key ?: 0
        return request.invoke(
            pageNum,
            params.loadSize
        ).fold(
            onSuccess = { value ->
                LoadResult.Page(
                    data = value,
                    prevKey = (pageNum - 1).takeIf { it >= 0 },
                    nextKey = (pageNum + 1).takeIf { value.size == params.loadSize }
                )
            },
            onFailure = { error ->
                LoadResult.Error(error)
            }
        )
    }
}
