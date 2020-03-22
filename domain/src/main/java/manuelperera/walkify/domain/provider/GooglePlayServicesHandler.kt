package manuelperera.walkify.domain.provider

import io.reactivex.Completable

interface GooglePlayServicesHandler {

    fun checkPlayServices(): Completable

}