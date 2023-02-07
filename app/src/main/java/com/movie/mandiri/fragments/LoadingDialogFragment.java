package com.movie.mandiri.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.movie.mandiri.R;

import javax.inject.Inject;

public class LoadingDialogFragment {

    private final AlertDialog dProgress;

    @Inject
    public LoadingDialogFragment(@NonNull Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ProgressDialogTheme)
                .setView(R.layout.progress_view)
                .setCancelable(false);
        dProgress = builder.create();
    }

    public void show() {
        dProgress.show();
    }

    public void dismiss() {
        dProgress.dismiss();
    }
}