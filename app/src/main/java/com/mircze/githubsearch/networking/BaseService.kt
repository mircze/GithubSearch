package com.mircze.githubsearch.networking

import com.mircze.githubsearch.networking.response.SearchRepositoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
interface BaseService {
    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String): Call<SearchRepositoriesResponse>
}
