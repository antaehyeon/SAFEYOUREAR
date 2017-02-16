package com.example.hyeon.safeyourear;

import android.app.Application;
import com.tsengvn.typekit.Typekit;

/*
https://github.com/tsengvn/typekit
https://blog.wonhada.com/?p=2001
 */

public class CustomStartApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "a_gothic_10.ttf"))
                .addBold(Typekit.createFromAsset(this, "a_gothic_10.ttf"));
    }
}
