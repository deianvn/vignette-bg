package com.github.deianvn.bg.vignette.utils.storage

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.github.deianvn.bg.vignette.utils.coroutines.DispatcherProvider
import com.squareup.moshi.Moshi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.reflect.KClass


class SharedPrefStorage(
    private val context: Context,
    private val moshi: Moshi
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

    @WorkerThread
    fun <T> putObject(key: String, value: T, javaType: Class<T>) {
        val json = moshi.adapter(javaType).toJson(value)
        putString(key, json)
    }

    @WorkerThread
    fun <T> getObject(key: String, javaType: Class<T>): T? {
        val json = getString(key) ?: return null
        return moshi.adapter(javaType).fromJson(json)
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
