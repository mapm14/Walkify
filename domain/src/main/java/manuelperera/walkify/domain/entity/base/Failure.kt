package manuelperera.walkify.domain.entity.base

import android.content.res.Resources
import manuelperera.walkify.domain.R

private const val UNKNOWN_ERROR_CODE = -1

sealed class Failure(var retryAction: () -> Unit = {}) : Throwable() {

    fun getMessage(resources: Resources): String = when (this) {
        is FailureWithMessage -> this.msg
        else -> resources.getString(R.string.unknown_error)
    }

    abstract class FailureWithMessage(open val msg: String) : Failure()

    class Error(val code: Int, override val msg: String) : FailureWithMessage(msg) {
        constructor(message: String) : this(UNKNOWN_ERROR_CODE, message)
    }

    class Timeout(override val msg: String) : FailureWithMessage(msg)

    class NoInternet(override val msg: String) : FailureWithMessage(msg)

    object Unauthorized : Failure()

    class FakeLocation(override val msg: String) : FailureWithMessage(msg)

    class ResolvableGooglePlayServicesError(val resultCode: Int) : Failure()

    object NoResolvableGooglePlayServicesError : Failure()

    object PermissionsNotGranted : Failure()

}