package com.movie.mandiri.presenters

import android.content.res.Resources
import com.movie.mandiri.R
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.models.*
import com.movie.mandiri.utils.retrofit2.APIConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class GenrePresenter : MovieGenreContract.Presenter {

    //param
    private val subscriptions = CompositeDisposable()
    private val srv = APIConnection.getInstance().service
    private lateinit var view: MovieGenreContract.View
    private var key = "859e1e2595ca61e03a724fb8889e0ddb"

    override fun getGenres() {
        view.onShowProgress()

        val subs = srv.getGenreMovie(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: GenreModel ->
                view.onHideProgress()

                view.onSuccess(model.genres)
            }, { error ->
                view.onHideProgress()
                view.onFailure(error)
            })

        subscriptions.add(subs)
    }

    override fun getByGenre(id: String?, page: String?) {
        view.onShowProgress()

        val subs = srv.getMovieByGenre(key, id, page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: ByGenreModel ->
                view.onHideProgress()

                if (model.totalResults!! > 0) {
                    view.onSuccess(model)
                } else {
                    view.onError(404, "Data tidak ada")
                }
            }, { error ->
                view.onHideProgress()
                view.onFailure(error)
            })

        subscriptions.add(subs)
    }

    override fun getDetail(id: String?, api_key: String?, language: String) {
        view.onShowProgress()

        val subs = srv.getMovieDetail(id, api_key, language).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: DetailModel ->
                view.onHideProgress()

                view.onSuccess(model)
            }, { error ->
                view.onHideProgress()
                view.onFailure(error)
            })

        subscriptions.add(subs)
    }

    override fun getTrailer(id: String?, api_key: String?, language: String) {
        view.onShowProgress()

        val subs = srv.getMovieTrailer(id, api_key, language).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: TrailerModel ->
                view.onHideProgress()

                view.onSuccess(model)
            }, { error ->
                view.onHideProgress()
                view.onFailure(error)
            })

        subscriptions.add(subs)
    }

    override fun getReview(id: String?, api_key: String?) {
        view.onShowProgress()

        val subs = srv.getMovieReview(id, api_key).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ model: ReviewModel ->
                view.onHideProgress()

                view.onSuccess(model)
            }, { error ->
                view.onHideProgress()
                view.onFailure(error)
            })

        subscriptions.add(subs)
    }

    override fun subsribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MovieGenreContract.View) {
        this.view = view
    }
}