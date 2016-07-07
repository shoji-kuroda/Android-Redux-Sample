package com.github.shoji_kuroda.android_redux_sample;

import android.support.constraint.BuildConfig;
import android.util.Log;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

/**
 * Created by kuroda02 on 2016/07/07.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Log
        Timber.plant(BuildConfig.DEBUG ? new DebugTree() : new CrashReportingTree());
    }

    private class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            if (t != null && priority == Log.ERROR) {
                // TODO: send Non-Fatal Error to crashlytics (fabric)
            }
        }
    }
}
