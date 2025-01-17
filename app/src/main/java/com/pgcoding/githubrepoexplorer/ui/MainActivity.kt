package com.pgcoding.githubrepoexplorer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.pgcoding.githubrepoexplorer.ui.composables.TopRepositoriesScaffold
import com.pgcoding.githubrepoexplorer.ui.theme.GitHubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: GitHubRepoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubExplorerTheme {
                TopRepositoriesScaffold(
                    onRefresh = { viewModel.fetchTopRepositories() }
                )
            }
        }
    }
}

