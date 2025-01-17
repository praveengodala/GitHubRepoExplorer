package com.pgcoding.githubrepoexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ContributorDto(
    @SerializedName("login")
    val username: String
)
