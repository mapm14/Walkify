package manuelperera.walkify.domain.provider

import io.reactivex.Observable
import manuelperera.walkify.domain.entity.location.GpsLocation

interface AndroidLocationProvider {

    fun locationUpdatesPeriodically(smallestDisplacementInMeters: Float): Observable<GpsLocation>

}