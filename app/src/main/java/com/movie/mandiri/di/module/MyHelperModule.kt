package com.movie.mandiri.di.module

import android.content.Context
import com.movie.mandiri.fragments.LoadingDialogFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class MyHelperModule {
    @Provides
    @Singleton
    fun provideLoadingDialog(context: Context): LoadingDialogFragment {
        return LoadingDialogFragment(context)
    }
}