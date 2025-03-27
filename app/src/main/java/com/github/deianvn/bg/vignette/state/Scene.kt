package com.github.deianvn.bg.vignette.state

import com.github.deianvn.bg.vignette.state.error.StateError


class Scene<T, U : Plot<U>>(
    val revision: Long = 0L,
    val act: T,
    val status: Status,
    private val plot: U,
    val fault: StateError? = null,
    private val previousHandler: () -> Unit = {}
) {
    private var previousScene: Scene<T, U>? = null

    private var remembered = false

    fun remember(): Scene<T, U> {
        remembered = true
        return this
    }

    fun previous(): Scene<T, U>? {
        return previousScene?.also {
            previousHandler()
        }
    }

    fun next(
        act: T = this.act,
        status: Status = this.status,
        plot: U = this.plot,
        fault: StateError? = this.fault,
        previousHandler: () -> Unit = this.previousHandler
    ) = Scene(
        revision = revision + 1L,
        act = act,
        status = status,
        plot = plot,
        fault = fault,
        previousHandler = previousHandler
    ).also {
        it.previousScene = if (remembered) {
            this
        } else {
            previousScene
        }
    }

    val plotCopy get() = plot.copyObject()
}
