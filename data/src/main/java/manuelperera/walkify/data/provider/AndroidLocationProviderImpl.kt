package manuelperera.walkify.data.provider

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Looper
import com.chuckerteam.chucker.BuildConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.data.R
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.location.GpsLocation
import manuelperera.walkify.domain.provider.AndroidLocationProvider
import timber.log.Timber
import javax.inject.Inject

class AndroidLocationProviderImpl @Inject constructor(
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val resources: Resources
) : AndroidLocationProvider {

    private lateinit var locationCallbackPeriodically: LocationCallback

    @SuppressLint("MissingPermission")
    override fun locationUpdatesPeriodically(timeInterval: Long): Observable<GpsLocation> {
        val locationRequestPeriodically: LocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = timeInterval
            fastestInterval = timeInterval
            smallestDisplacement = 100f // TODO: Check this variable
        }

        return Observable.create<GpsLocation> { emitter ->
                locationCallbackPeriodically = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        if (emitter.isDisposed.not()) {
                            locationResult?.locations?.forEach { location ->
                                Timber.w("Location mocked result: ${location.isFromMockProvider}")

                                @Suppress("ConstantConditionIf")
                                if (BuildConfig.BUILD_TYPE != "debug" && location.isFromMockProvider) {
                                    emitter.onError(Failure.FakeLocation(resources.getString(R.string.location_mocked_message)))
                                } else {
                                    val gpsLocation = GpsLocation(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        isLastKnownLocation = false,
                                        accuracy = location.accuracy
                                    )
                                    emitter.onNext(gpsLocation)
                                }
                            }
                        }
                    }
                }

                Looper.prepare()
                fusedLocationProvider.requestLocationUpdates(
                    locationRequestPeriodically,
                    locationCallbackPeriodically,
                    Looper.myLooper()
                )
                Looper.loop()
            }
            .doAfterTerminate { stopLocationUpdatesPeriodically() }
            .doOnDispose { stopLocationUpdatesPeriodically() }
    }

    override fun stopLocationUpdatesPeriodically(): Completable = Completable.fromAction {
        if (::locationCallbackPeriodically.isInitialized)
            fusedLocationProvider.removeLocationUpdates(locationCallbackPeriodically)
    }

}