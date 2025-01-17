package com.pgcoding.githubrepoexplorer.domain.models

data class RepositoryWithTopContributor(
    val id: Long,
    val repoName: String,
    val stars: Int,
    val topContributorName: String?
)
