package com.example.model;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences{
    private static final String SHARED_DATA = "shared_data";
    private static MySharedPreferences mySharedPreferences;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        if(mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    public String getString(String name, String defaultResult) {
        return sharedPreferences.getString(name, defaultResult);
    }

    public void putString(String name, String value) {
        editor.putString(name, value);
        editor.apply();
    }
}
