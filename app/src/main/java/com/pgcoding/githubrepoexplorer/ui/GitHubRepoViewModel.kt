package com.pgcoding.githubrepoexplorer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.models.Repository
import com.pgcoding.githubrepoexplorer.domain.usecases.GitHubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubRepoViewModel @Inject constructor(private val gitHubUseCase: GitHubUseCase): ViewModel() {

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val uiState: LiveData<UiState> get() = _uiState

    /**
     * Fetch Repositories with top contributors for each repository
     */
    fun fetchTopRepositories() {
        viewModelScope.launch {
            // set initial ui state to loading
            _uiState.value = UiState.Loading

            when(val result = gitHubUseCase.getRepositories()) {
                is APIResult.Success -> {
                    val repositories: List<Repository> = result.data.filterIsInstance<Repository>() // Only keep instances of Repository

                    // Update UI with rest of repository data while top contributors are still loading
                    _uiState.value = UiState.Success(repositories)

                    // Fetch top contributors and update UI.
                    // I believe Compose keeps track of the data changes and update fields accordingly
                    _uiState.value = UiState.Success(gitHubUseCase.getRepositoriesWithTopContributors(repositories))
                }
                is APIResult.Failure -> {
                    _uiState.value = UiState.Failure(R.string.generic_error)
                }
            }
        }
    }
}