package manuelperera.walkify.data.repository.photo.datasource

import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.data.datasource.local.AppDatabase
import manuelperera.walkify.data.entity.photo.database.PhotoEntityDb
import manuelperera.walkify.domain.entity.photo.Photo
import javax.inject.Inject

class PhotoLocalDataSource @Inject constructor(
    private val database: AppDatabase
) {

    fun getPhotoListUpdates(): Observable<List<Photo>> {
        return database.photoDao()
            .getAll()
            .map { photoDbList ->
                photoDbList.map { it.toDomain() }
            }
    }

    fun insert(photo: Photo) {
        val lastInserted: PhotoEntityDb? = database.photoDao().getLastInserted()
        if (photo.id != lastInserted?.id) {
            database.photoDao().insertAll(PhotoEntityDb(photo))
        }
    }

    fun clearDatabase(): Completable {
        return database.photoDao().deleteAll()
    }

}