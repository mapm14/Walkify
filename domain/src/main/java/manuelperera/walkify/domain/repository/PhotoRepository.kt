package manuelperera.walkify.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.Photo

interface PhotoRepository {

    fun getPhotoIdByLocation(latitude: Double, longitude: Double): Single<String>

    fun getPhotoById(id: String): Single<Photo>

    fun getPhotoUpdates(): Observable<List<Photo>>

    fun clearDatabase(): Completable

}