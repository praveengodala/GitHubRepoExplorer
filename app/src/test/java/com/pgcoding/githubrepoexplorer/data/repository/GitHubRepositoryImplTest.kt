package com.pgcoding.githubrepoexplorer.data.repository

import com.pgcoding.githubrepoexplorer.Description
import com.pgcoding.githubrepoexplorer.data.remote.GitHubApiService
import com.pgcoding.githubrepoexplorer.data.remote.dto.ContributorDto
import com.pgcoding.githubrepoexplorer.data.remote.dto.RepositoryDto
import com.pgcoding.githubrepoexplorer.data.remote.dto.RepositoryDto.OwnerDto
import com.pgcoding.githubrepoexplorer.data.remote.dto.RepositoryResponseDto
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GitHubRepositoryImplTest {

    private lateinit var repository: GitHubRepositoryImpl
    private lateinit var mockApiService: GitHubApiService

    @Before
    fun setUp() {
        mockApiService = mockk(relaxed = true)
        repository = GitHubRepositoryImpl(mockApiService)
    }

    @Test
    @Description("Test getRepositories returns success with mapped data")
    fun testGetRepositories_Success() = runTest {
        // Arrange
        coEvery {
            mockApiService.getTopRepositories()
        } returns RepositoryResponseDto(
            repositories = listOf(
                RepositoryDto(
                    id = 1,
                    name = "Repo1",
                    owner = OwnerDto(name = "Owner1"),
                    stars = 100
                ),
                RepositoryDto(
                    id = 2,
                    name = "Repo2",
                    owner = OwnerDto(name = "Owner2"),
                    stars = 200
                )
            )
        )

        // Act
        val result = repository.getRepositories()

        // Assert
        assertTrue(result is APIResult.Success)
        val successResult = result as APIResult.Success
        assertEquals(2, successResult.data.size)
        assertEquals("Repo1", successResult.data[0].repoName)
        assertEquals(100, successResult.data[0].stars)
        assertEquals("Owner1", successResult.data[0].ownerName)
    }

    @Test
    @Description("Test getRepositories returns failure on exception")
    fun testGetRepositories_Failure() = runTest {
        // Arrange
        coEvery { mockApiService.getTopRepositories() } throws Exception("Network Error")

        // Act
        val result = repository.getRepositories()

        // Assert
        assertTrue(result is APIResult.Failure)
        assertEquals("Network Error", (result as APIResult.Failure).exception.message)
    }

    @Test
    @Description("Test getTopContributor returns success with first contributor")
    fun testGetTopContributor_Success() = runTest {
        // Arrange
        coEvery {
            mockApiService.getTopContributor("Owner1", "Repo1")
        } returns listOf(
            ContributorDto(username = "TopContributor1"),
            ContributorDto(username = "TopContributor2")
        )

        // Act
        val result = repository.getTopContributor("Owner1", "Repo1")

        // Assert
        assertTrue(result is APIResult.Success)
        assertEquals("TopContributor1", (result as APIResult.Success).data?.name)
    }

    @Test
    @Description("Test getTopContributor returns success with null if no contributors")
    fun testGetTopContributor_Success_Null() = runTest {
        // Arrange
        coEvery { mockApiService.getTopContributor("Owner1", "Repo1") } returns emptyList()

        // Act
        val result = repository.getTopContributor("Owner1", "Repo1")

        // Assert
        assertTrue(result is APIResult.Success)
        assertNull((result as APIResult.Success).data)
    }

    @Test
    @Description("Test getTopContributor returns failure on exception")
    fun testGetTopContributor_Failure() = runTest {
        // Arrange
        coEvery { mockApiService.getTopContributor("Owner1", "Repo1") } throws Exception("API Error")

        // Act
        val result = repository.getTopContributor("Owner1", "Repo1")

        // Assert
        assertTrue(result is APIResult.Failure)
        assertEquals("API Error", (result as APIResult.Failure).exception.message)
    }
}