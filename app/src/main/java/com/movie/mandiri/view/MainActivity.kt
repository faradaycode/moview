package com.movie.mandiri.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.movie.mandiri.App
import com.movie.mandiri.R
import com.movie.mandiri.adapters.GenreAdapter
import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.databinding.ActivityMainBinding
import com.movie.mandiri.fragments.LoadingDialogFragment
import com.movie.mandiri.models.Genre
import com.movie.mandiri.utils.AppException
import com.movie.mandiri.utils.GridColumnSpacer
import com.movie.mandiri.utils.Kata
import com.movie.mandiri.utils.MyHelpers
import com.movie.mandiri.utils.VerticalLinearDecoration
import com.movie.mandiri.utils.prefs.PrefImplementer
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    MovieGenreContract.View, GenreAdapter.GenreView {

    private lateinit var context: Context

    //dagger
    @Inject
    lateinit var preference: PrefImplementer

    @Inject
    lateinit var loadingDialogFragment: LoadingDialogFragment

    private var mLastClickTime: Long = 0
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var presenter: MovieGenreContract.Presenter

    private var adapter = GenreAdapter(arrayListOf(), this)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        //dagger2 go!!!
        (application as App).getAppComponent(context).inject(this)
        //injection
        presenter.attach(this)
        presenter.subsribe()

        inita()
    }

    private fun inita() {
        binding.reviGenre.layoutManager = GridLayoutManager(context, 2)
        binding.reviGenre.setHasFixedSize(true)
        binding.reviGenre.addItemDecoration(GridColumnSpacer(8))
        binding.reviGenre.adapter = adapter
        presenter.getGenres()
    }

    override fun onSuccess(datum: Any?) {
        if (datum is List<*>) {
            val dataa: List<Genre> = datum.filterIsInstance<Genre>()
            try {
                adapter.addAll(dataa as MutableList<Genre>)
            } catch (e: AppException) {
                e.printStackTrace()
                MyHelpers.toaster(context, "Error: ${e.message}")
            }
        } else {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder
                .setMessage("09: Terjadi kesalahan pada aplikasi")
                .setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok)) { dialog, _ ->
                    val data = Intent()
                    setResult(RESULT_CANCELED, data)
                    dialog.dismiss()
                    finish()
                }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun onShowProgress() {
        loadingDialogFragment.show()
    }

    override fun onHideProgress() {
        loadingDialogFragment.dismiss()
    }

    override fun onError(code: Int, message: String) {
        MyHelpers.toaster(context, message)
    }

    override fun onFailure(t: Throwable) {
        t.printStackTrace()
        MyHelpers.AlertDialoger(context, MyHelpers.FailureRetrofit(t))
    }

    override fun clickGenre(id: Int) {
        val intent = Intent(context, FilmActivity::class.java)
        val b = Bundle()
        b.putInt("genre", id)
        intent.putExtras(b)
        startActivity(intent)
    }
}