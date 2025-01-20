package com.pgcoding.githubrepoexplorer.domain.models

data class RepositoryWithTopContributor(
    override val id: Long,
    override val repoName: String,
    override val stars: Int,
    val topContributorName: String?
): RepositoryState()
