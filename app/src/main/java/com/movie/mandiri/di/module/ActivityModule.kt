package com.movie.mandiri.di.module

import com.movie.mandiri.contracts.MovieGenreContract
import com.movie.mandiri.presenters.GenrePresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    fun provideGenrePresenter(): MovieGenreContract.Presenter {
        return GenrePresenter()
    }
}