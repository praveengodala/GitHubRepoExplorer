package com.pgcoding.githubrepoexplorer.domain.repository

import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.Contributor
import com.pgcoding.githubrepoexplorer.domain.models.Repository

interface GitHubRepository {
    suspend fun getRepositories(): APIResult<List<Repository>>
    suspend fun getTopContributor(owner: String, repo: String): APIResult<Contributor?>
}