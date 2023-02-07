package com.movie.mandiri.utils.prefs

import android.content.Context
import com.movie.mandiri.utils.Kata
import javax.inject.Inject

class PrefImplementer @Inject constructor(application: Context) : PrefInterface {

    private var preference = application.getSharedPreferences(
        Kata.PRF,
        Context.MODE_PRIVATE
    )
    private var editor = preference.edit()

    override fun getString(key: String, defaultValue: String): String {
        return preference.getString(key, defaultValue) ?: defaultValue
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return preference.getInt(key, defaultValue) ?: defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preference.getBoolean(key, defaultValue) ?: defaultValue
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return preference.getFloat(key, defaultValue) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override fun putInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    override fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    override fun putFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    override fun clearPref() {
        editor.clear().apply()
    }

    override fun contains(key: String): Boolean {
        return preference.contains(key)
    }
}