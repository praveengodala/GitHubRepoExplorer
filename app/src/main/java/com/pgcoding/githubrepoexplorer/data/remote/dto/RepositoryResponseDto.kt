package com.pgcoding.githubrepoexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RepositoryResponseDto(
    @SerializedName("items")
    val repositories: List<RepositoryDto>
)

data class RepositoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: OwnerDto,
    @SerializedName("stargazers_count")
    val stars: Int
) {
    data class OwnerDto(
        @SerializedName("login")
        val name: String
    )
}