import com.github.deianvn.bg.vignette.R
import com.github.deianvn.bg.vignette.state.error.ClientError
import com.github.deianvn.bg.vignette.state.error.NetworkError
import com.github.deianvn.bg.vignette.state.error.ServerError
import com.github.deianvn.bg.vignette.state.error.StateError


fun getErrorMessageResource(error: StateError?) = when(error) {
    is NetworkError -> R.string.error_network_error
    is ClientError -> R.string.error_app_compatibility
    is ServerError -> R.string.error_server
    else -> R.string.error_general
}
