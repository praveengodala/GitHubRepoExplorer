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
    @Description("Test getTopRepositories succeeds when getRepositories calls succeed")
    fun testGetRepositories_Success()  = runTest {
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
        }

        // Act
        val result = gitHubUseCase.getRepositories()

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals(
            listOf(
                Repository(1001, "Repo1", "Owner1", 100),
                Repository(1002, "Repo2", "Owner2", 98)
            ),
            (result as APIResult.Success).data
        )
    }

    @Test
    @Description("Test getTopRepositories fails when getRepositories call fails")
    fun testGetRepositories_Failure()  = runTest {
        // Arrange
        val mockException = Exception("Failed to fetch repositories")
        coEvery { mockRepository.getRepositories() } returns APIResult.Failure(mockException)

        // Act
        val result = gitHubUseCase.getRepositories()

        // Assert
        assert(result is APIResult.Failure)
        assertEquals(mockException, (result as APIResult.Failure).exception)
    }

    @Test
    @Description("Test getTopRepositories succeeds when getTopContributor calls succeed")
    fun testGetRepositoriesWithTopContributors_Success()  = runTest {
        // Arrange
        mockRepository.apply {
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
        val result = gitHubUseCase.getRepositoriesWithTopContributors(
            listOf(
                Repository(id = 1001, repoName = "Repo1", ownerName = "Owner1", stars = 100),
                Repository(id = 1002, repoName = "Repo2", ownerName = "Owner2", stars = 98)
            )
        )

        // Assert
        assertEquals(
            listOf(
                RepositoryWithTopContributor(1001, "Repo1", 100, "TopContributor1"),
                RepositoryWithTopContributor(1002, "Repo2", 98, "TopContributor2")
            ),
            result
        )
    }



    @Test
    @Description("Test getTopRepositories succeeds even when some getTopContributor calls fails")
    fun testGetRepositories_PartialSuccess() = runTest {
        // Arrange
        mockRepository.apply {
            coEvery {
                getTopContributor("Owner1", "Repo1")
            } returns APIResult.Failure(
                Exception("Failed to fetch top contributor")
            )
            coEvery {
                getTopContributor("Owner2", "Repo2")
            } returns APIResult.Success(
                Contributor(name = "TopContributor2")
            )
        }

        // Act
        val result = gitHubUseCase.getRepositoriesWithTopContributors(
            listOf(
                Repository(id = 1001, repoName = "Repo1", ownerName = "Owner1", stars = 100),
                Repository(id = 1002, repoName = "Repo2", ownerName = "Owner2", stars = 98)
            )
        )

        // Assert
        assertEquals(
            listOf(
                RepositoryWithTopContributor(1001, "Repo1", 100, null),
                RepositoryWithTopContributor(1002, "Repo2", 98, "TopContributor2")
            ),
            result
        )
    }
}