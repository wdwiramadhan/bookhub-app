package com.example.bookhub;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    Map config = new HashMap();

    private void configCloudinary() {
        config.put("cloud_name", "bidik-kampus");
        config.put("api_key", "481532558188319");
        config.put("api_secret", "XSGns0ZvIUOzNlgEQyRkEwiF9hw");
        MediaManager.init(MyApp.this, config);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        configCloudinary();
    }
}
