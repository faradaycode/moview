package com.movie.mandiri.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class DisableErrorWatcher implements TextWatcher {

    TextInputLayout view;

    public DisableErrorWatcher(TextInputLayout view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        view.setError("");
        view.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
