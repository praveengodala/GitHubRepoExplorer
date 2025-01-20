package com.pgcoding.githubrepoexplorer.domain.usecases

import com.pgcoding.githubrepoexplorer.data.repository.GitHubRepositoryImpl
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.Repository
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryState
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitHubUseCase @Inject constructor(private val repository: GitHubRepositoryImpl) {

    /**
     * Fetch top repositories
     */
    suspend fun getRepositories(): APIResult<List<RepositoryState>> {
        return withContext(Dispatchers.IO) {
            repository.getRepositories()
        }
    }

    /**
     * Fetch Top contributors for given [repositories]
     * If any of the contributor calls were to fail, we treat it as null and continue executing rest of the calls
     */
    suspend fun getRepositoriesWithTopContributors(repositories: List<Repository>): List<RepositoryState> {
        return withContext(Dispatchers.IO) {
            repositories.map { repo ->
                // async helps make multiple getTopContributor calls concurrently
                // and in parallel depending on the IO dispatcher thread pool
                async {
                    val contributorResult = repository.getTopContributor(repo.ownerName, repo.repoName)
                    RepositoryWithTopContributor(
                        id = repo.id,
                        repoName = repo.repoName,
                        stars = repo.stars,
                        topContributorName = when (contributorResult) {
                            is APIResult.Success -> contributorResult.data?.name
                            is APIResult.Failure -> null
                        }
                    )
                }
            }.awaitAll()
        }
    }
}