package com.pgcoding.githubrepoexplorer.domain.core

sealed class APIResult<out T> {
    class Success<T>(val data: T) : APIResult<T>()
    class Failure<T>(val exception: Throwable) : APIResult<T>()
}