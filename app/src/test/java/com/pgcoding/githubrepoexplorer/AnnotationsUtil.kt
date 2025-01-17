package com.pgcoding.githubrepoexplorer

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Description(val value: String = "")