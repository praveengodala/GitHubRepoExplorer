package com.pgcoding.githubrepoexplorer.domain.usecases

import com.pgcoding.githubrepoexplorer.data.repository.GitHubRepositoryImpl
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GitHubUseCase @Inject constructor(private val repository: GitHubRepositoryImpl) {

    suspend fun getTopRepositories(): APIResult<List<RepositoryWithTopContributor>> {
        return withContext(Dispatchers.IO) {
            when (val reposResult = repository.getRepositories()) {
                is APIResult.Success -> {
                    APIResult.Success(
                        // For each repository, an async call is made to fetch the top contributor.
                        // This means 100 parallel network requests will be initiated, one for each repository.
                        reposResult.data.map { repo ->
                            async {
                                val contributor = repository.getTopContributor(repo.ownerName, repo.repoName)
                                RepositoryWithTopContributor(
                                    id = repo.id,
                                    repoName = repo.repoName,
                                    stars = repo.stars,
                                    topContributorName = when (contributor) {
                                        is APIResult.Success -> contributor.data?.name
                                        is APIResult.Failure -> null
                                    }
                                )
                            }
                        }.awaitAll() // wait for all 100 contributor fetches to complete.
                    )
                }
                is APIResult.Failure -> APIResult.Failure(reposResult.exception)
            }
        }
    }
}