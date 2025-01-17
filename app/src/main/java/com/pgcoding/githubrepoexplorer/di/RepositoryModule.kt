package com.pgcoding.githubrepoexplorer.di

import com.pgcoding.githubrepoexplorer.data.repository.GitHubRepositoryImpl
import com.pgcoding.githubrepoexplorer.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGitHubRepository(
        impl: GitHubRepositoryImpl
    ): GitHubRepository
}