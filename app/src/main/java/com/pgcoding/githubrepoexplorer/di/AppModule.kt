package com.pgcoding.githubrepoexplorer.di

import android.content.Context
import com.pgcoding.githubrepoexplorer.R
import com.pgcoding.githubrepoexplorer.data.interceptors.AccessTokenInterceptor
import com.pgcoding.githubrepoexplorer.data.remote.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val GITHUB_BASE_URL = "https://api.github.com/"

    // okhttp cache size
    private const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        // set up caching
        val cacheDirectory = File(context.cacheDir, "http_cache")
        val cache = Cache(cacheDirectory, CACHE_SIZE)

        // this resource comes from auto generated gradleResValues.xml and not the regular strings.xml
        val token = context.resources.getString(R.string.github_token)

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(AccessTokenInterceptor(token))
            .build()
    }

    @Provides
    @Singleton
    fun providesGitHubApiService(okHttpClient: OkHttpClient): GitHubApiService {
        return Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }
}