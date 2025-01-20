package com.pgcoding.githubrepoexplorer.domain.models

sealed class RepositoryState {
    abstract val id: Long
    abstract val repoName: String
    abstract val stars: Int
}