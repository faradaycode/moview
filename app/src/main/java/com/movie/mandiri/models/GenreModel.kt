package com.movie.mandiri.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class GenreModel {
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>? = null
}

class Genre {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

}