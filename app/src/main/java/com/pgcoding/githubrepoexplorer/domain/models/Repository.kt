package com.pgcoding.githubrepoexplorer.domain.models

data class Repository(
    val id: Long,
    val repoName: String,
    val ownerName: String,
    val stars: Int
)
