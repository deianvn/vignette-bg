package com.github.deianvn.bg.vignette.state

import retrofit2.HttpException
import java.io.IOException


data class Scene<T, U : Plot<U>>(
    val revision: Long = 0L,
    val act: T,
    val status: Status,
    private val plot: U,
    val fault: Throwable? = null
) {
    fun next() = copy(
        revision = revision + 1L
    )

    fun next(act: T, status: Status): Scene<T, U> = copy(
        revision = revision + 1L,
        act = act,
        status = status,
        fault = null
    )

    fun success(): Scene<T, U> = copy(
        revision = revision + 1L,
        status = Status.SUCCESS
    )

    fun fail(fault: Throwable): Scene<T, U> = copy(
        revision = revision + 1L,
        status = Status.ERROR, fault = fault
    )

    fun loading() = copy(
        revision = revision + 1L,
        status = Status.LOADING
    )

    val plotCopy get() = plot.copyObject()
}

fun Throwable?.isAuthError() = this is HttpException && this.code() == 401

fun Throwable?.isForbiddenError() = this is HttpException && this.code() == 403

fun Throwable?.isNetworkError() = this is IOException
