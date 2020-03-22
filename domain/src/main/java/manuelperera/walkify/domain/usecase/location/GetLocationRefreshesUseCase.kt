package manuelperera.walkify.domain.usecase.location

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import manuelperera.walkify.domain.entity.location.GpsLocation
import manuelperera.walkify.domain.provider.AndroidLocationProvider
import manuelperera.walkify.domain.usecase.base.ObservableUseCase
import javax.inject.Inject

/**
 * Would return [manuelperera.walkify.domain.entity.base.Failure.PermissionsNotGranted]
 * if [android.Manifest.permission.ACCESS_FINE_LOCATION] isn't granted
 */
class GetLocationRefreshesUseCase @Inject constructor(
    private val androidLocationProvider: AndroidLocationProvider
) : ObservableUseCase<GpsLocation, GetLocationRefreshesParams> {

    override fun invoke(params: GetLocationRefreshesParams): Observable<GpsLocation> =
        androidLocationProvider
            .locationUpdatesPeriodically(params.smallestDisplacementInMeters)
            .subscribeOn(Schedulers.newThread())

}

inline class GetLocationRefreshesParams(val smallestDisplacementInMeters: Float)