package com.mircze.githubsearch.networking.response

import com.mircze.githubsearch.model.GithubRepo

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
data class SearchRepositoriesResponse(val items: List<GithubRepo>)
