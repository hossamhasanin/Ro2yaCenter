package com.hasanin.hossam.ro2yacenter;

import android.app.Application;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mohamed on 10/07/2018.
 */

public class Ro2yaCenterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}
