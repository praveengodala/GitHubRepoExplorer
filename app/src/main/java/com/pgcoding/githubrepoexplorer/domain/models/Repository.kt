package com.pgcoding.githubrepoexplorer.domain.models

data class Repository(
    override val id: Long,
    override val repoName: String,
    val ownerName: String,
    override val stars: Int
): RepositoryState()