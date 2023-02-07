package com.movie.mandiri.di.component

import com.movie.mandiri.databinding.FragmentMovieDetailsBinding
import com.movie.mandiri.di.module.*
import com.movie.mandiri.fragments.MovieDetailsFragment
import com.movie.mandiri.view.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        SharedPreferenceModule::class,
        MyHelperModule::class,
        ActivityModule::class,
        FragmentModule::class,
    ]
)
interface AppComponent {
    //tujuan inject
    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(filmActivity: FilmActivity)
    fun inject(trailerActivity: TrailerActivity)
    fun inject(reviewActivity: ReviewActivity)

    //fragment
    fun inject(movieDetailsFragment: MovieDetailsFragment)
}