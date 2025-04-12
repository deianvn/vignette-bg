package com.github.deianvn.bg.vignette.state.act


sealed class AppAct : Act()

class PrerequisitesAct : AppAct()

class VignetteListAct(
    val isNewVignetteRequested: Boolean = false,
    val isRefreshing: Boolean = false
) : AppAct()
