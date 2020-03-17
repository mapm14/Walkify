package manuelperera.walkify.data.repository.photo

import io.reactivex.Single
import manuelperera.walkify.data.repository.photo.datasource.PhotoRemoteDataSource
import manuelperera.walkify.domain.repository.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoRemoteDataSource: PhotoRemoteDataSource
) : PhotoRepository {

    override fun getPhotoByLocation(latitude: Double, longitude: Double): Single<String> {
        return photoRemoteDataSource.getPhotoByLocation(latitude, longitude)
    }

}