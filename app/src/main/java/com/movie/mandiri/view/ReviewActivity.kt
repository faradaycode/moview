package com.movie.mandiri.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.movie.mandiri.App
import com.movie.mandiri.R
import com.movie.mandiri.adapters.MoviesAdapter
import com.movie.mandiri.adapters.ReviewAdapter
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.databinding.ActivityReviewBinding
import com.movie.mandiri.fragments.LoadingDialogFragment
import com.movie.mandiri.fragments.MovieDetailsFragment
import com.movie.mandiri.models.ByGenreModel
import com.movie.mandiri.models.Result
import com.movie.mandiri.models.ResultReview
import com.movie.mandiri.models.ReviewModel
import com.movie.mandiri.utils.MyHelpers
import com.movie.mandiri.utils.PaginationOnScrollListener
import com.movie.mandiri.utils.VerticalLinearDecoration
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReviewActivity : AppCompatActivity(), MovieGenreContract.View, MoviesAdapter.ByGenreView {

    private var binding: ActivityReviewBinding? = null

    @Inject
    lateinit var presenter: MovieGenreContract.Presenter

    @Inject
    lateinit var loader: LoadingDialogFragment

    private var PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START

    private var id_genre: String = ""
    private var adapter: ReviewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        (application as App).getAppComponent(this).inject(this)
        //injection
        presenter.attach(this)
        presenter.subsribe()

        adapter = ReviewAdapter(arrayListOf())

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
                    this@ReviewActivity.isLoading = true
                    currentPage++
                    presenter.getReview(id_genre.toString(), currentPage.toString())
                }

                override fun getTotalPageCount(): Int {
                    return TOTAL_PAGES
                }

                override fun isLastPage(): Boolean {
                    return this@ReviewActivity.isLastPage
                }

                override fun isLoading(): Boolean {
                    return this@ReviewActivity.isLoading
                }
            })

            id_genre = b.getString("id", "")
            presenter.getReview(id_genre.toString(), getString(R.string.apimoviedb))
        }
    }

    override fun onSuccess(datum: Any?) {
        adapter?.removeLoadingFooter() //selesai, buang loading
        this@ReviewActivity.isLoading = false

        if (datum is ReviewModel) {
            val dataa: List<ResultReview>? = datum.results

            adapter?.addAll(dataa)
            TOTAL_PAGES = datum.total_pages!!

            if (currentPage == PAGE_START) {
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@ReviewActivity.isLastPage = true
                }
            } else {
//                adapter?.removeLoadingFooter() //selesai, buang loading
//                this@FilmActivity.isLoading = false
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@ReviewActivity.isLastPage = true
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
        this@ReviewActivity.isLastPage = true
        this@ReviewActivity.isLoading = false
        adapter?.removeLoadingFooter()
    }

    override fun onFailure(t: Throwable) {
        t.printStackTrace()
        this@ReviewActivity.isLoading = false
        this@ReviewActivity.isLastPage = true
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