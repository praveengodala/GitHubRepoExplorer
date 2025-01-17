package com.pgcoding.githubrepoexplorer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pgcoding.githubrepoexplorer.Description
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor
import com.pgcoding.githubrepoexplorer.domain.usecases.GitHubUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GitHubRepoViewModelTest {

    /**
     * Ensures that all LiveData/Architecture Components operations
     * happen synchronously in the same thread during testing
     */
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGitHubUseCase: GitHubUseCase = mockk(relaxed = true)
    private lateinit var viewModel: GitHubRepoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = GitHubRepoViewModel(mockGitHubUseCase)
    }

    @After
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain() // Reset the Main dispatcher after the test
    }

    @Test
    @Description("Test FetchTopRepositories emit success state when api call succeeds")
    fun testFetchTopRepositories_Success()  = runTest {
        coEvery {
            mockGitHubUseCase.getTopRepositories()
        } returns APIResult.Success(createMockRepositories())

        // assert that beginning state is UiState.Loading before fetch
        assertEquals(UiState.Loading, viewModel.uiState.value)

        viewModel.fetchTopRepositories()

        // assert that state changes to UiState.Success after successful fetch
        assertTrue(viewModel.uiState.value is UiState.Success)

        // assert data
        assertArrayEquals(
            arrayOf(
                RepositoryWithTopContributor(
                    id = 1000,
                    repoName = "TopRepo1",
                    stars = 100,
                    topContributorName = "TopContributor1"
                ),
                RepositoryWithTopContributor(
                    id = 1001,
                    repoName = "TopRepo2",
                    stars = 98,
                    topContributorName = "TopContributor2"
                )
            ),
            (viewModel.uiState.value as UiState.Success).repositories.toTypedArray()
        )
    }

    @Test
    @Description("Test FetchTopRepositories emit failure state when api call fails")
    fun testFetchTopRepositories_Failure() = runTest {
        coEvery { mockGitHubUseCase.getTopRepositories() } returns APIResult.Failure(Exception())

        // assert that beginning state is UiState.Loading before fetch
        assertEquals(UiState.Loading, viewModel.uiState.value)

        viewModel.fetchTopRepositories()

        // assert that state changes to UiState.Failure on failing to fetch top repositories
        assertTrue(viewModel.uiState.value is UiState.Failure)

        // assert failure message
        assertEquals(R.string.generic_error, (viewModel.uiState.value as UiState.Failure).messageResId)
    }

    //================================================================================
    // Helper methods
    //================================================================================

    private fun createMockRepositories(): List<RepositoryWithTopContributor> {
        return listOf(
            RepositoryWithTopContributor(
                id = 1000,
                repoName = "TopRepo1",
                stars = 100,
                topContributorName = "TopContributor1"
            ),
            RepositoryWithTopContributor(
                id = 1001,
                repoName = "TopRepo2",
                stars = 98,
                topContributorName = "TopContributor2"
            )
        )
    }
}