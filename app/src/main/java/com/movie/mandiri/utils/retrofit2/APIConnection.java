package com.movie.mandiri.utils.retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movie.mandiri.utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConnection {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static APIInterface service;
    private static APIConnection serviceGenerator;

    private APIConnection() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(APIInterface.class);
    }

    public static APIConnection getInstance() {
        if (serviceGenerator == null) {
            serviceGenerator = new APIConnection();
        }
        return serviceGenerator;
    }

    public APIInterface getService() {
        return service;
    }

}
