package com.github.deianvn.my.vignette.preferences

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.WorkerThread

class SharedPrefStorage(
    private val context: Context
) {

    companion object {
        private const val APP_STORAGE = "com.github.deianvn.my.vignette"
    }

    @WorkerThread
    fun putString(key: String, value: String) {
        context.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE).edit()
            .putString(key, value).apply()
    }

    @WorkerThread
    fun getString(key: String): String? {
        return context.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
            .getString(key, null)
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    fun clear(key: String) {
        context.getSharedPreferences(APP_STORAGE, Context.MODE_PRIVATE)
            .edit()
            .remove(key)
            .commit()
    }

}
