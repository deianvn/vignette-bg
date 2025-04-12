package com.github.deianvn.bg.vignette.state

import com.github.deianvn.bg.vignette.state.error.StateError


class Scene<A, P : Plot<P>>(
    val revision: Long = 0L,
    val act: A,
    val status: Status,
    private val plot: P,
    val fault: StateError? = null,
    private val previousHandler: () -> Unit = {}
) {

    private var previousScene: Scene<A, P>? = null

    private var remembered = false


    fun remember(): Scene<A, P> {
        remembered = true
        return this
    }

    fun previous(): Scene<A, P>? {
        return previousScene?.also {
            previousHandler()
        }
    }

    fun next(
        status: Status,
        act: A = this.act,
        plot: P = this.plot,
        fault: StateError? = null,
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
