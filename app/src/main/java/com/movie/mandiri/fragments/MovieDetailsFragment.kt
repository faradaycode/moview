package com.movie.mandiri.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.movie.mandiri.App
import com.movie.mandiri.R
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.databinding.FragmentMovieDetailsBinding
import com.movie.mandiri.models.DetailModel
import com.movie.mandiri.utils.MyHelpers
import com.movie.mandiri.view.ReviewActivity
import com.movie.mandiri.view.TrailerActivity
import javax.inject.Inject

class MovieDetailsFragment : BottomSheetDialogFragment(), MovieGenreContract.View {

    private var mLastClickTime: Long = 0

    @Inject
    lateinit var presenter: MovieGenreContract.Presenter

    @Inject
    lateinit var loader: LoadingDialogFragment

    private lateinit var binding: FragmentMovieDetailsBinding

    private var ide: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).getAppComponent(context).inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subsribe()

        val b = arguments

        if (b != null) {
            ide = b.getString("id", "")
            presenter.getDetail(ide, getString(R.string.apimoviedb))
        }
    }

    override fun onSuccess(datum: Any?) {
        if (datum is DetailModel) {
            val drawable = CircularProgressDrawable(requireContext())
            drawable.centerRadius = 30f
            drawable.strokeWidth = 5f
            // set all other properties as you would see fit and start it
            // set all other properties as you would see fit and start it
            drawable.start()

            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500/${datum.posterPath}")
                .placeholder(drawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.imposter)

            val genrr: MutableList<String> = arrayListOf()

            for (dd in datum.genres!!) {
                genrr.add(dd.name.toString())
            }

            binding.tvGenre.text = genrr.joinToString(", ")
            binding.tvBahasa.text = datum.spokenLanguages?.get(0)?.englishName
            binding.tvHomprod.text = datum.productionCompanies?.get(0)?.name
            binding.tvjudul.text = datum.originalTitle
            binding.tvVote.text = datum.voteCount.toString()
            binding.tvRelease.text = datum.releaseDate
            binding.tvStatus.text = datum.status

            binding.fabVideo.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener
                }

                mLastClickTime = SystemClock.elapsedRealtime()

                val ind = Intent(requireActivity(), TrailerActivity::class.java)
                val b = Bundle()
                b.putString("id", ide ?: "0")
                ind.putExtras(b)
                startActivity(ind)
            }

            binding.btnReview.setOnClickListener {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener
                }

                mLastClickTime = SystemClock.elapsedRealtime()

                val ind = Intent(requireActivity(), ReviewActivity::class.java)
                val b = Bundle()
                b.putString("id", ide ?: "0")
                ind.putExtras(b)
                startActivity(ind)
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
        MyHelpers.toaster(requireContext(), "$code: $message")
    }

    override fun onFailure(t: Throwable) {
        MyHelpers.FailureRetrofit(t)
    }
}