package manuelperera.walkify.domain.repository

import io.reactivex.Single
import manuelperera.walkify.domain.entity.photo.PhotoSizeInfo

interface PhotoRepository {

    fun getPhotoIdByLocation(latitude: Double, longitude: Double): Single<String>

    fun getPhotoById(id: String): Single<List<PhotoSizeInfo>>

}