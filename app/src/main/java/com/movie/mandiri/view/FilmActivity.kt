package com.movie.mandiri.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.movie.mandiri.App
import com.movie.mandiri.adapters.MoviesAdapter
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.databinding.ActivityFilmBinding
import com.movie.mandiri.fragments.LoadingDialogFragment
import com.movie.mandiri.fragments.MovieDetailsFragment
import com.movie.mandiri.models.ByGenreModel
import com.movie.mandiri.models.Result
import com.movie.mandiri.utils.MyHelpers
import com.movie.mandiri.utils.PaginationOnScrollListener
import com.movie.mandiri.utils.VerticalLinearDecoration
import javax.inject.Inject

class FilmActivity : AppCompatActivity(), MovieGenreContract.View, MoviesAdapter.ByGenreView {

    private var binding: ActivityFilmBinding? = null

    @Inject
    lateinit var presenter: MovieGenreContract.Presenter

    @Inject
    lateinit var loader: LoadingDialogFragment

    private var PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START

    private var id_genre: Int = 0
    private var adapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        (application as App).getAppComponent(this).inject(this)
        //injection
        presenter.attach(this)
        presenter.subsribe()

        adapter = MoviesAdapter(arrayListOf(), this)

        onInita()
    }

    private fun onInita() {
        val b = intent.extras
        val linearLayoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        if (b != null) {
            binding?.rvMovie?.layoutManager = linearLayoutManager
            binding?.rvMovie?.addItemDecoration(VerticalLinearDecoration(16))
            binding?.rvMovie?.setHasFixedSize(true)
            binding?.rvMovie?.adapter = adapter

            binding?.rvMovie?.addOnScrollListener(object :
                PaginationOnScrollListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    this@FilmActivity.isLoading = true
                    currentPage++
                    presenter.getByGenre(id_genre.toString(), currentPage.toString())
                }

                override fun getTotalPageCount(): Int {
                    return TOTAL_PAGES
                }

                override fun isLastPage(): Boolean {
                    return this@FilmActivity.isLastPage
                }

                override fun isLoading(): Boolean {
                    return this@FilmActivity.isLoading
                }
            })

            id_genre = b.getInt("genre", 0)
            presenter.getByGenre(id_genre.toString(), PAGE_START.toString())
        }
    }

    override fun onSuccess(datum: Any?) {
        adapter?.removeLoadingFooter() //selesai, buang loading
        this@FilmActivity.isLoading = false

        if (datum is ByGenreModel) {
            val dataa: List<Result>? = datum.results

            adapter?.addAll(dataa)
            TOTAL_PAGES = datum.totalPages!!

            if (currentPage == PAGE_START) {
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@FilmActivity.isLastPage = true
                }
            } else {
//                adapter?.removeLoadingFooter() //selesai, buang loading
//                this@FilmActivity.isLoading = false
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@FilmActivity.isLastPage = true
                }
            }
        }
    }

    override fun onShowProgress() {
//        loader.show()
    }

    override fun onHideProgress() {
//        loader.dismiss()
    }

    override fun onError(code: Int, message: String) {
        this@FilmActivity.isLastPage = true
        this@FilmActivity.isLoading = false
        adapter?.removeLoadingFooter()
    }

    override fun onFailure(t: Throwable) {
        t.printStackTrace()
        this@FilmActivity.isLoading = false
        this@FilmActivity.isLastPage = true
        adapter?.removeLoadingFooter()

        MyHelpers.FailureRetrofit(t)
    }

    override fun clickFilm(id: Int) {
        val b = Bundle()
        b.putString("id", "$id")
        val fr = MovieDetailsFragment()
        fr.arguments = b
        fr.show(supportFragmentManager, "detail_film")
    }
}