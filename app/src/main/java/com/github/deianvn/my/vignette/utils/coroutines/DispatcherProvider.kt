package com.github.deianvn.my.vignette.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val ui: CoroutineDispatcher
}
