package com.pgcoding.githubrepoexplorer.domain.usecases

import com.pgcoding.githubrepoexplorer.Description
import com.pgcoding.githubrepoexplorer.data.repository.GitHubRepositoryImpl
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.Contributor
import com.pgcoding.githubrepoexplorer.domain.models.Repository
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class GitHubUseCaseTest {

    private val mockRepository: GitHubRepositoryImpl = mockk(relaxed = true)
    private lateinit var gitHubUseCase: GitHubUseCase

    @Before
    fun setUp() {
        gitHubUseCase = GitHubUseCase(mockRepository)
    }

    @Test
    @Description("Test getTopRepositories succeeds when getRepositories and getTopContributor calls succeed")
    fun testGetTopRepositories_Success()  = runTest {
        // Arrange
        mockRepository.apply {
            coEvery {
                getRepositories()
            } returns  APIResult.Success(
                listOf(
                    Repository(id = 1001, repoName = "Repo1", ownerName = "Owner1", stars = 100),
                    Repository(id = 1002, repoName = "Repo2", ownerName = "Owner2", stars = 98)
                )
            )
            coEvery {
                getTopContributor("Owner1", "Repo1")
            } returns APIResult.Success(
                Contributor(name = "TopContributor1")
            )
            coEvery {
                mockRepository.getTopContributor("Owner2", "Repo2")
            } returns APIResult.Success(
                Contributor(name = "TopContributor2")
            )
        }

        // Act
        val result = gitHubUseCase.getTopRepositories()

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals(
            listOf(
                RepositoryWithTopContributor(1001, "Repo1", 100, "TopContributor1"),
                RepositoryWithTopContributor(1002, "Repo2", 98, "TopContributor2")
            ),
            (result as APIResult.Success).data
        )
    }

    @Test
    @Description("Test getTopRepositories fails when getRepositories call fails")
    fun testGetTopRepositories_Failure()  = runTest {
        // Arrange
        val mockException = Exception("Failed to fetch repositories")
        coEvery { mockRepository.getRepositories() } returns APIResult.Failure(mockException)

        // Act
        val result = gitHubUseCase.getTopRepositories()

        // Assert
        assert(result is APIResult.Failure)
        assertEquals(mockException, (result as APIResult.Failure).exception)
    }

    @Test
    @Description("Test getTopRepositories succeeds even when some getTopContributor calls fails")
    fun testGetTopRepositories_PartialSuccess() = runTest {
        // Arrange
        mockRepository.apply {
            coEvery {
                getRepositories()
            } returns  APIResult.Success(
                listOf(
                    Repository(id = 1001, repoName = "Repo1", ownerName = "Owner1", stars = 100),
                    Repository(id = 1002, repoName = "Repo2", ownerName = "Owner2", stars = 98)
                )
            )
            coEvery {
                getTopContributor("Owner1", "Repo1")
            } throws Exception("Exception")

//            returns APIResult.Failure(
//                Exception("Failed to fetch top contributor")
//            )
            coEvery {
                getTopContributor("Owner2", "Repo2")
            } returns APIResult.Success(
                Contributor(name = "TopContributor2")
            )
        }

        // Act
        val result = gitHubUseCase.getTopRepositories()

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals(
            listOf(
                RepositoryWithTopContributor(1001, "Repo1", 100, null),
                RepositoryWithTopContributor(1002, "Repo2", 98, "TopContributor2")
            ),
            (result as APIResult.Success).data
        )
    }
}