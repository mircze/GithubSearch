package com.mircze.githubsearch.networking

import com.mircze.githubsearch.model.GithubRepo

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
interface DataSource {
    fun getRepositoriesByQuery(query: String, callback: ApiCallback<List<GithubRepo>>)
}
