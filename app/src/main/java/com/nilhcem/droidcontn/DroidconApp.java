package com.nilhcem.droidcontn;

import android.app.Application;
import android.content.Context;

import com.nilhcem.droidcontn.core.dagger.AppComponent;

import timber.log.Timber;

public class DroidconApp extends Application {

    private AppComponent component;

    public static DroidconApp get(Context context) {
        return (DroidconApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initGraph();
        initLogger();
    }

    public AppComponent component() {
        return component;
    }

    private void initGraph() {
        component = AppComponent.Initializer.init(this);
    }

    private void initLogger() {
        Timber.plant(new Timber.DebugTree());
    }
}