package ru.sicampus.bootcamp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListPagingDto(
    @SerialName("content")
    val content: List<UserDto>?
)
