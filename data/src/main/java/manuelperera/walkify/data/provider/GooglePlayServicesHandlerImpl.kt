package manuelperera.walkify.data.provider

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.Completable
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.provider.GooglePlayServicesHandler
import javax.inject.Inject

class GooglePlayServicesHandlerImpl @Inject constructor(
    private val context: Context
) : GooglePlayServicesHandler {

    override fun checkPlayServices(): Completable = Completable.create { emitter ->
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)

        when {
            resultCode != ConnectionResult.SUCCESS -> {
                val isResolvableError = apiAvailability.isUserResolvableError(resultCode)
                val failure = if (isResolvableError) {
                    Failure.ResolvableGooglePlayServicesError(resultCode)
                } else {
                    Failure.NoResolvableGooglePlayServicesError
                }
                emitter.tryOnError(failure)
            }
            else -> emitter.onComplete()
        }
    }

}