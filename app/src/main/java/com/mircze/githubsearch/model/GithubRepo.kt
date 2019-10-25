package com.mircze.githubsearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Mirosław Juda on 24.10.2019.
 */
@Parcelize
data class GithubRepo(val id: Int? = null,
                      val name: String? = null,
                      val description: String? = null,
                      val language: String? = null,
                      @SerializedName("stargazers_count")
                      val stars: String? = null,
                      @SerializedName("html_url")
                      val htmlUrl: String? = null): Parcelable {
}
