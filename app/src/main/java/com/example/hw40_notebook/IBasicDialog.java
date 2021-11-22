package com.example.hw40_notebook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public interface IBasicDialog {

    default  void showBasicDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        @SuppressLint("DefaultLocale")
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .create();
        alertDialog.setMessage(message);
        alertDialog.setTitle(title);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                listener);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> dialog.dismiss());

        alertDialog.setContentView(R.layout.support_simple_spinner_dropdown_item);
        alertDialog.show();
    }
}
