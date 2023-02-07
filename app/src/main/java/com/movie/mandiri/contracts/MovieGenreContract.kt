package com.movie.mandiri.contracts

import com.movie.mandiri.models.Genre
import com.movie.mandiri.models.GenreModel
import com.movie.mandiri.models.SpokenLanguage
import com.movie.mandiri.utils.BaseContract

class MovieGenreContract {
    interface View : BaseContract.View {
        fun onSuccess(datum: Any?)

        fun onShowProgress()

        fun onHideProgress()

        fun onError(code: Int, message: String)

        fun onFailure(t: Throwable)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getGenres()
        fun getByGenre(id: String?, page: String?)
        fun getDetail(id: String?, api_key: String?, language: String = "id")
        fun getTrailer(id: String?, api_key: String?, language: String = "en-US")
        fun getReview(id: String?, api_key: String?)
    }
}