package com.mircze.githubsearch.networking

import com.mircze.githubsearch.model.GithubRepo
import com.mircze.githubsearch.networking.response.SearchRepositoriesResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
class ApiManager private constructor(): DataSource {

    companion object {
        val instance = ApiManager()
    }

    private val retrofit: Retrofit
    private val baseService: BaseService

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        baseService = retrofit.create()
    }

    //region DataSource
    override fun getRepositoriesByQuery(query: String, callback: ApiCallback<List<GithubRepo>>) {
        baseService.searchRepositories(query).enqueue(object: Callback<SearchRepositoriesResponse> {
            override fun onFailure(call: Call<SearchRepositoriesResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<SearchRepositoriesResponse>, response: Response<SearchRepositoriesResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.items ?: emptyList())
                } else {
                    callback.onError(Throwable(response.errorBody().toString()))
                }
            }
        })
    }
    //endregion
}
