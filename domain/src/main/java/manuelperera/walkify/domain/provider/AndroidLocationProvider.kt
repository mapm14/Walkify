package manuelperera.walkify.domain.provider

import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.domain.entity.location.GpsLocation

interface AndroidLocationProvider {

    fun locationUpdatesPeriodically(timeInterval: Long): Observable<GpsLocation>

    fun stopLocationUpdatesPeriodically(): Completable

}