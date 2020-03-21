package manuelperera.walkify.data.repository.photo.datasource

import io.reactivex.Single
import manuelperera.walkify.data.datasource.remote.BaseRemoteDataSource
import manuelperera.walkify.data.repository.photo.data.PhotoApi
import manuelperera.walkify.domain.entity.photo.Photo
import javax.inject.Inject

class PhotoRemoteDataSource @Inject constructor(
    private val photoApi: PhotoApi
) : BaseRemoteDataSource() {

    fun getPhotoIdByLocation(latitude: Double, longitude: Double): Single<String> {
        return modifySingle(photoApi.getPhotoIdByLocation(latitude, longitude))
    }

    fun getPhotoById(id: String): Single<Photo> {
        return modifySingle(photoApi.getPhotoById(id)).map { Photo(id, it) }
    }

}