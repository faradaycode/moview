package com.movie.mandiri.di.module

import com.movie.mandiri.utils.prefs.PrefImplementer
import com.movie.mandiri.utils.prefs.PrefInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class SharedPreferenceModule {
    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(prefImplementer: PrefImplementer): PrefInterface {
        return prefImplementer
    }
}