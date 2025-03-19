package com.github.deianvn.bg.vignette.state.step


sealed class Act

class Prerequisites : Act()

class VignetteList(
    val isNewVignetteRequested: Boolean = false
) : Act()
