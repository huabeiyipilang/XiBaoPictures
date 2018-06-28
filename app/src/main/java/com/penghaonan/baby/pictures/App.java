package com.penghaonan.baby.pictures;

import android.app.Application;

import com.penghaonan.appframework.AppDelegate;
import com.penghaonan.appframework.reporter.Reporter;
import com.penghaonan.baby.pictures.reporter.UmengReporter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.init(this);
        AppDelegate.setDebug(BuildConfig.DEBUG);
        Reporter.getInstance().addReporter(new UmengReporter());
    }
}
