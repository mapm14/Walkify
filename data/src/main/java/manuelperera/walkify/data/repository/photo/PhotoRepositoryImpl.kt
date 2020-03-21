package manuelperera.walkify.data.repository.photo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import manuelperera.walkify.data.repository.photo.datasource.PhotoLocalDataSource
import manuelperera.walkify.data.repository.photo.datasource.PhotoRemoteDataSource
import manuelperera.walkify.domain.entity.photo.Photo
import manuelperera.walkify.domain.repository.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoRemoteDataSource: PhotoRemoteDataSource,
    private val photoLocalDataSource: PhotoLocalDataSource
) : PhotoRepository {

    override fun getPhotoIdByLocation(latitude: Double, longitude: Double): Single<String> {
        return photoRemoteDataSource.getPhotoIdByLocation(latitude, longitude)
    }

    override fun getPhotoById(id: String): Single<Photo> {
        return photoRemoteDataSource.getPhotoById(id)
            .doOnSuccess(photoLocalDataSource::setPhotoSizeInfoUpdates)
    }

    override fun getPhotoUpdates(): Observable<List<Photo>> {
        return photoLocalDataSource.getPhotoSizeInfoUpdates()
    }

    override fun clearDatabase(): Completable {
        return photoLocalDataSource.clearDatabase()
    }

}