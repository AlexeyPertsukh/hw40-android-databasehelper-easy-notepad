package com.example.util;

import android.util.Log;

public interface ILog {
    String KEY_LOG = "MY_CONTROL";
    default void printLog(String message) {
        Log.d(KEY_LOG, message);
    }
}
