package com.pgcoding.githubrepoexplorer.data.remote

import com.pgcoding.githubrepoexplorer.data.remote.dto.ContributorDto
import com.pgcoding.githubrepoexplorer.data.remote.dto.RepositoryResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    /**
     * Fetches the top repositories from GitHub based on given query params.
     *
     * @param query The search query string. Default is "stars:>0", which means repositories with more than 0 stars.
     * @param sort sort by parameter. Default is "stars", which sorts repositories by their star count.
     * @param order The order of sorting. Default is "desc", which means results are sorted in descending order.
     * @param perPage results per page. Default is 100, which is the maximum allowed by the GitHub API and matches the exercise specification.
     * @return [RepositoryResponseDto]
     */
    @GET("search/repositories")
    suspend fun getTopRepositories(
        @Query("q") query: String = "stars:>0",
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 100
    ): RepositoryResponseDto

    /**
     * Fetches the list of contributors for a given [repo]. Default is a list with only one i.e Top contributor
     * As per documentation, this api returns contributors sorted by the number of commits per contributor in descending order.
     * @param owner name of repository owner.
     * @param repo name of repository.
     * @param perPage number of contributors to return. Default is 1 to fetch only the top contributor to match the exercise specification.
     * @return list of [ContributorDto]
     */
    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getTopContributor(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("per_page") perPage: Int = 1 // Top contributor
    ): List<ContributorDto>
}