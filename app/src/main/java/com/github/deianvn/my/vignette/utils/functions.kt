package com.github.deianvn.my.vignette.utils

import android.content.Context
import okio.buffer
import okio.source

fun Context.readResourceAsString(resourceId: Int): String {
    val inputStream = resources.openRawResource(resourceId)
    return inputStream.source().buffer().readUtf8()
}
