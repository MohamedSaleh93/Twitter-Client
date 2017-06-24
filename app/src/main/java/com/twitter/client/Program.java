package com.twitter.client;

import android.app.Application;
import android.content.Context;

/**
 * @author MohamedSaleh on 5/27/2017.
 */

public class Program extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
