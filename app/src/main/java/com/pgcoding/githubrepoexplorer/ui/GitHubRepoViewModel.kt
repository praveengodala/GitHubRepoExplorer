package com.pgcoding.githubrepoexplorer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.domain.core.APIResult
import com.pgcoding.githubrepoexplorer.domain.usecases.GitHubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitHubRepoViewModel @Inject constructor(private val gitHubUseCase: GitHubUseCase): ViewModel() {

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val uiState: LiveData<UiState> get() = _uiState

    fun fetchTopRepositories() {
        viewModelScope.launch {
            // set initial ui state to loading
            _uiState.value = UiState.Loading

            when(val result = gitHubUseCase.getTopRepositories()) {
                is APIResult.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is APIResult.Failure -> {
                    _uiState.value = UiState.Failure(R.string.generic_error)
                }
            }
        }
    }
}