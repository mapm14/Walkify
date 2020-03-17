package manuelperera.walkify.domain.repository

import io.reactivex.Single

interface PhotoRepository {

    fun getPhotoByLocation(latitude: Double, longitude: Double): Single<String>

}