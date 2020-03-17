package manuelperera.walkify.data.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Looper
import com.chuckerteam.chucker.BuildConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import io.reactivex.Observable
import manuelperera.walkify.data.R
import manuelperera.walkify.domain.entity.base.Failure
import manuelperera.walkify.domain.entity.location.GpsLocation
import manuelperera.walkify.domain.provider.AndroidLocationProvider
import timber.log.Timber
import javax.inject.Inject

private const val REFRESH_INTERVAL_IN_MILLIS = 60000L

class AndroidLocationProviderImpl @Inject constructor(
    private val fusedLocationProvider: FusedLocationProviderClient,
    private val resources: Resources,
    private val context: Context
) : AndroidLocationProvider {

    private lateinit var locationCallbackPeriodically: LocationCallback

    override fun locationUpdatesPeriodically(smallestDisplacementInMeters: Float): Observable<GpsLocation> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return Observable.error(Failure.PermissionsNotGranted)
        } else {
            val locationRequestPeriodically: LocationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = REFRESH_INTERVAL_IN_MILLIS
                fastestInterval = REFRESH_INTERVAL_IN_MILLIS / 2
                smallestDisplacement = smallestDisplacementInMeters
            }

            return Observable.create<GpsLocation> { emitter ->
                    locationCallbackPeriodically = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
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

                    Looper.prepare()
                    fusedLocationProvider.requestLocationUpdates(
                        locationRequestPeriodically,
                        locationCallbackPeriodically,
                        Looper.myLooper()
                    )
                    Looper.loop()
                }
                .doAfterTerminate { stopLocationUpdatesPeriodically() }
        }
    }

    override fun stopLocationUpdatesPeriodically() {
        if (::locationCallbackPeriodically.isInitialized) {
            fusedLocationProvider.removeLocationUpdates(locationCallbackPeriodically)
        }
    }

}