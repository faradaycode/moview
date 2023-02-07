package com.movie.mandiri.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private var application: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}