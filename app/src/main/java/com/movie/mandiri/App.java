package com.movie.mandiri;

import android.app.Application;
import android.content.Context;

import com.movie.mandiri.di.component.AppComponent;
import com.movie.mandiri.di.component.DaggerAppComponent;
import com.movie.mandiri.di.module.ApplicationModule;
import com.movie.mandiri.di.module.MyHelperModule;

public class App extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppComponent getAppComponent(Context context) {
        appComponent = DaggerAppComponent
                .builder()
                .applicationModule(new ApplicationModule(context))
                .myHelperModule(new MyHelperModule())
                .build();

        return appComponent;
    }
}
