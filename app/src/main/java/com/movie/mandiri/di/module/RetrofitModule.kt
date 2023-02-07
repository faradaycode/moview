package com.movie.mandiri.di.module

import com.movie.mandiri.utils.retrofit2.APIInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {
        val BASE_URL = "http://192.168.1.200/TRegApi/index.php/API/";
        val ROOT_URL = "http://192.168.1.200/TRegApi/";
//    val BASE_URL = "https://bkdpendapatan.depok.go.id/TRegApi/index.php/API/"
//    val ROOT_URL = "https://bkdpendapatan.depok.go.id/TRegApi/"

    //    public static final String ROOT_URL = "http://36.94.102.87:50080/TRegApi/";
    //    public static final String BASE_URL = "http://36.94.102.87:50080/TRegApi/index.php/API/";
    val DIR_METER = "gambar/pendataan/meteran/"
    val DIR_SLIDER = "gambar/carrousel/"
    val DIR_OPFOTO = ROOT_URL + "gambar/op/"
    val DIR_TTDFOTO = ROOT_URL + "gambar/signatures/"

    @Provides
    @Singleton
    fun provideEndpoint(okHttpClient: OkHttpClient): APIInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(APIInterface::class.java)
    }
}