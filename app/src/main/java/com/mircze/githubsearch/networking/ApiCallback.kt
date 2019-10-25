package com.mircze.githubsearch.networking

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
interface ApiCallback<T> {
    fun onSuccess(result: T)
    fun onError(exception: Throwable?)
}

