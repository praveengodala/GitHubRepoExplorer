package com.pgcoding.githubrepoexplorer.data.repository

import com.pgcoding.githubrepoexplorer.data.remote.GitHubApiService
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.Contributor
import com.pgcoding.githubrepoexplorer.domain.models.Repository
import com.pgcoding.githubrepoexplorer.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService
): GitHubRepository {

    // Fetch repositories
    override suspend fun getRepositories(): APIResult<List<Repository>> {
        return try {
            APIResult.Success(
                apiService.getTopRepositories().repositories.map {
                    Repository(
                        id = it.id,
                        repoName = it.name,
                        ownerName = it.owner.name,
                        stars = it.stars
                    )
                }
            )
        } catch (exception: Exception) {
            APIResult.Failure(exception)
        }
    }

    override suspend fun getTopContributor(owner: String, repo: String): APIResult<Contributor?> {
        return try {
            APIResult.Success(
                apiService.getTopContributor(owner, repo).map {
                    Contributor(name = it.username)
                }.firstOrNull()
            )
        } catch (exception: Exception) {
            APIResult.Failure(exception)
        }
    }
}