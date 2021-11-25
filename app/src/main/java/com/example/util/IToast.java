package com.example.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw40_notebook.R;

public interface IToast {

    int OFFSET_X = 0;
    int OFFSET_Y = 40;

    default void shortToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    default void longToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    default void showToast(Context context, String message, int duration) {
        Toast toast = new Toast(context);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        toast.setView(view);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, OFFSET_X, OFFSET_Y);
        toast.setDuration(duration);
        toast.show();
    }
}
