package com.github.deianvn.bg.vignette.utils.preferences

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.core.content.edit


class SharedPrefStorage(
    private val context: Context
) {

    companion object {
        private const val APP_STORAGE = "com.github.deianvn.bg.vignette"
    }

    @WorkerThread
    fun putString(key: String, value: String) {
        getSharedPreferences().edit(commit = true) {
            putString(key, value)
        }
    }

    @WorkerThread
    fun getString(key: String): String? {
        return getSharedPreferences()
            .getString(key, null)
    }

    fun putStringSet(key: String, value: Set<String>) {
        getSharedPreferences().edit(commit = true) {
            putStringSet(key, value)
        }
    }

    fun getStringSet(key: String): Set<String>? {
        return getSharedPreferences()
            .getStringSet(key, null)
    }

    @WorkerThread
    fun clear(key: String) {
        getSharedPreferences()
            .edit(commit = true) {
                remove(key)
            }
    }

    private fun getSharedPreferences() = context.getSharedPreferences(
        APP_STORAGE, Context.MODE_PRIVATE
    )

}