package com.pgcoding.githubrepoexplorer.ui

import androidx.annotation.StringRes
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor

sealed interface UiState {
    data object Loading: UiState
    data class Success(val repositories: List<RepositoryWithTopContributor>): UiState
    data class Failure(@StringRes val messageResId: Int): UiState
}