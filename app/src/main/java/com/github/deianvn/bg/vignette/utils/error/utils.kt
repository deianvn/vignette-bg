package com.github.deianvn.bg.vignette.utils.error

import com.github.deianvn.bg.vignette.state.error.AuthenticationError
import com.github.deianvn.bg.vignette.state.error.AuthorizationError
import com.github.deianvn.bg.vignette.state.error.ClientError
import com.github.deianvn.bg.vignette.state.error.NetworkError
import com.github.deianvn.bg.vignette.state.error.ServerError
import com.github.deianvn.bg.vignette.state.error.GeneralError
import com.github.deianvn.bg.vignette.state.error.StateError
import retrofit2.HttpException
import java.io.IOException


fun toStateError(error: Throwable): StateError = when {
    error is StateError -> error
    error is IOException -> NetworkError(error)
    error is HttpException && error.code() == 401 -> AuthenticationError(error)
    error is HttpException && error.code() == 403 -> AuthorizationError(error)
    error is HttpException && error.code() in 400..499 -> ClientError(error)
    error is HttpException && error.code() in 500..599 -> ServerError(error)
    else -> GeneralError(error)
}
