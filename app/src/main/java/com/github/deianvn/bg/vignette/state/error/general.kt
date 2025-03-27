package com.github.deianvn.bg.vignette.state.error

import okio.IOException
import retrofit2.HttpException

sealed class StateError(
    throwable: Throwable? = null
) : IllegalStateException(
    throwable?.message, throwable
)

class GeneralError(
    throwable: Throwable? = null,
) : StateError(throwable)

class AuthenticationError(val exception: HttpException) : StateError(exception)

class AuthorizationError(val exception: HttpException) : StateError(exception)

class ClientError(val exception: HttpException) : StateError(exception)

class ServerError(val exception: HttpException) : StateError(exception)

class NetworkError(val exception: IOException) : StateError(exception)
