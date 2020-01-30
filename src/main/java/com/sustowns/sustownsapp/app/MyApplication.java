package com.sustowns.sustownsapp.app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.sustowns.sustownsapp.helpers.TypefaceUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        mInstance = this;

        myUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);

        // Typeface
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/GT Walsheim Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        @SuppressLint({"NewApi", "LocalSuppress"}) StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public static Thread.UncaughtExceptionHandler myUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            for (StackTraceElement element : ex.getStackTrace()) {
                Log.e("RRR", element.toString());
            }
        }
    };

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
