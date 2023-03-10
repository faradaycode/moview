package com.movie.mandiri.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class TrailerModel {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("results")
    @Expose
    val results: List<TrailerDatum>? = null

}

class TrailerDatum {

    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String? = null

    @SerializedName("iso_3166_1")
    @Expose
    val iso31661: String? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("key")
    @Expose
    val key: String? = null

    @SerializedName("site")
    @Expose
    val site: String? = null

    @SerializedName("size")
    @Expose
    val size: Int? = null

    @SerializedName("type")
    @Expose
    val type: String? = null

    @SerializedName("official")
    @Expose
    val official: Boolean? = null

    @SerializedName("published_at")
    @Expose
    val publishedAt: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

}