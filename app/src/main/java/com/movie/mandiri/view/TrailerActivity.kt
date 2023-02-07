package com.movie.mandiri.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.movie.mandiri.App
import com.movie.mandiri.R
import com.movie.mandiri.adapters.MoviesAdapter
import com.movie.mandiri.adapters.TrailerAdapter
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.databinding.ActivityTrailerBinding
import com.movie.mandiri.fragments.LoadingDialogFragment
import com.movie.mandiri.models.Result
import com.movie.mandiri.models.TrailerDatum
import com.movie.mandiri.models.TrailerModel
import com.movie.mandiri.utils.MyHelpers
import com.movie.mandiri.utils.PaginationOnScrollListener
import com.movie.mandiri.utils.VerticalLinearDecoration
import javax.inject.Inject

class TrailerActivity : AppCompatActivity(), MovieGenreContract.View, TrailerAdapter.TrailerView {
    private lateinit var binding: ActivityTrailerBinding

    @Inject
    lateinit var presenter: MovieGenreContract.Presenter

    @Inject
    lateinit var loader: LoadingDialogFragment

    private var PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START

    private var id_genre: String? = null
    private var adapter: TrailerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).getAppComponent(this).inject(this)
        //injection
        presenter.attach(this)
        presenter.subsribe()

        inita()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun inita() {
        val b = intent.extras

        if (b != null) {
            val linearLayoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false
            )

            id_genre = b.getString("id", "")
            adapter = TrailerAdapter(arrayListOf(), this)
            binding.revideo.layoutManager = linearLayoutManager
            binding.revideo.addItemDecoration(VerticalLinearDecoration(16))
            binding.revideo.setHasFixedSize(true)
            binding.revideo.adapter = adapter

            binding.revideo.addOnScrollListener(object :
                PaginationOnScrollListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    this@TrailerActivity.isLoading = true
                    currentPage++
                    presenter.getByGenre(id_genre.toString(), currentPage.toString())
                }

                override fun getTotalPageCount(): Int {
                    return TOTAL_PAGES
                }

                override fun isLastPage(): Boolean {
                    return this@TrailerActivity.isLastPage
                }

                override fun isLoading(): Boolean {
                    return this@TrailerActivity.isLoading
                }
            })

            presenter.getTrailer(id_genre.toString(), getString(R.string.apimoviedb))
        }
    }

    override fun onSuccess(datum: Any?) {
        adapter?.removeLoadingFooter() //selesai, buang loading

        if (datum is TrailerModel) {
            val dataa: List<TrailerDatum>? = datum.results

            adapter?.addAll(dataa)
            TOTAL_PAGES = 1

            if (currentPage == PAGE_START) {
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@TrailerActivity.isLastPage = true
                }
            } else {
//                adapter?.removeLoadingFooter() //selesai, buang loading
//                this@TrailerActivity.isLoading = false
                if (currentPage <= TOTAL_PAGES) {
                    adapter?.addLoadingFooter()
                } else {
                    this@TrailerActivity.isLastPage = true
                }
            }
        }
    }

    override fun onShowProgress() {
        loader.show()
    }

    override fun onHideProgress() {
        loader.dismiss()
    }

    override fun onError(code: Int, message: String) {
        this@TrailerActivity.isLastPage = true
        this@TrailerActivity.isLoading = false
        adapter?.removeLoadingFooter()
    }

    override fun onFailure(t: Throwable) {
        t.printStackTrace()
        this@TrailerActivity.isLoading = false
        this@TrailerActivity.isLastPage = true
        adapter?.removeLoadingFooter()

        MyHelpers.FailureRetrofit(t)
    }

    override fun clickFilm(id: String) {
        val url = "https://www.youtube.com/watch?v=$id"
        var intent: Intent?

        try {
            // get the Twitter app if possible
            this.packageManager.getPackageInfo("com.google.android.youtube", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        }

        startActivity(intent)
    }
}