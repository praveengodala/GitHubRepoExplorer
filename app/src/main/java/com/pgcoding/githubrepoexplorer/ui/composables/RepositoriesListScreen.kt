package com.pgcoding.githubrepoexplorer.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.domain.models.Repository
import com.pgcoding.githubrepoexplorer.domain.models.RepositoryWithTopContributor
import com.pgcoding.githubrepoexplorer.ui.GitHubRepoViewModel
import com.pgcoding.githubrepoexplorer.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRepositoriesScaffold(
    viewModel: GitHubRepoViewModel = viewModel(),
    onRefresh: () -> Unit
) {
    val uiState by viewModel.uiState.observeAsState(UiState.Loading)

    // Trigger the fetch function only when the screen is shown and
    // data is not already loaded
    if (uiState is UiState.Loading || uiState is UiState.Failure) {
        LaunchedEffect(Unit) {
            viewModel.fetchTopRepositories()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarColors(
                    containerColor = colorResource(R.color.colorBlue),
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                ),
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        TopRepositories(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onRefresh = onRefresh
        )
    }
}

@Composable
fun TopRepositories(
    modifier: Modifier,
    uiState: UiState,
    onRefresh: () -> Unit
) {
    when(uiState) {
        is UiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.colorBlue))
            }
        }
        is UiState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = uiState.repositories,
                    key = { it.id }
                ) { repositoryState ->
                    RepositoryItem(
                        modifier = Modifier.padding(16.dp),
                        repoName = repositoryState.repoName,
                        stars = repositoryState.stars,
                        topContributorName = when (repositoryState) {
                            is Repository -> {
                                // loading placeholder. Can be improved to show
                                // popular shimmer placeholder while the data loads
                                stringResource(R.string.generic_loading_text)
                            }

                            is RepositoryWithTopContributor -> {
                                repositoryState.topContributorName
                            }
                        }
                    )
                    HorizontalDivider(color = colorResource(R.color.colorGray2))
                }
            }
        }
        is UiState.Failure -> {
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(uiState.messageResId),
                onRefresh = onRefresh
            )
        }
    }
}