package com.movie.mandiri.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ReviewModel {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("page")
    @Expose
    val page: Int? = null

    @SerializedName("results")
    @Expose
    val results: List<ResultReview>? = null

    @SerializedName("total_pages")
    @Expose
    val total_pages: Int? = null

    @SerializedName("total_results")
    @Expose
    val totalResult: Int? = null
}

class ResultReview {
    @SerializedName("author")
    @Expose
    val author: String? = null

    @SerializedName("author_details")
    @Expose
    val authorDetails: AuthorDetails? = null

    @SerializedName("content")
    @Expose
    val content: String? = null

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null

    @SerializedName("url")
    @Expose
    val url: String? = null
}

class AuthorDetails {
    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("username")
    @Expose
    val username: String? = null

    @SerializedName("avatar_path")
    @Expose
    val avatarPath: String? = null

    @SerializedName("rating")
    @Expose
    val rating: Double? = null

}