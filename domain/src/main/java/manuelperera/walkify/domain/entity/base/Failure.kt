package manuelperera.walkify.domain.entity.base

private const val UNKNOWN_ERROR_CODE = -1
private const val UNKNOWN_REASON = "UNKNOWN REASON"

sealed class Failure(var retryAction: () -> Unit = {}) : Throwable() {

    abstract class FailureWithMessage(open val msg: String) : Failure()

    class Error(val code: Int, override val msg: String, val reason: String) : FailureWithMessage(msg) {
        constructor(message: String) : this(UNKNOWN_ERROR_CODE, message, UNKNOWN_REASON)
        constructor(message: String, reason: String) : this(UNKNOWN_ERROR_CODE, message, reason)
    }

    class Timeout(override val msg: String) : FailureWithMessage(msg)

    class NoInternet(override val msg: String) : FailureWithMessage(msg)

    object Unauthorized : Failure()

    class FakeLocation(override val msg: String) : FailureWithMessage(msg)

    class ResolvableGooglePlayServicesError(val resultCode: Int) : Failure()

    object NoResolvableGooglePlayServicesError : Failure()

}