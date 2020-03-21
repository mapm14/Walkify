package manuelperera.walkify.data.repository.photo.datasource

import android.content.Context
import androidx.room.Room
import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.data.datasource.local.AppDatabase
import manuelperera.walkify.data.entity.photo.database.PhotoEntityDb
import manuelperera.walkify.domain.entity.photo.Photo
import javax.inject.Inject
import javax.inject.Singleton

private const val WALKIFY_DATABASE = "WALKIFY_DATABASE"

@Singleton
class PhotoLocalDataSource @Inject constructor(
    context: Context
) {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            WALKIFY_DATABASE
        ).build()
    }

    fun getPhotoSizeInfoUpdates(): Observable<List<Photo>> {
        return database.photoDao()
            .getAll()
            .map { photoDbList ->
                photoDbList.map { it.toDomain() }
            }
    }

    fun setPhotoSizeInfoUpdates(photo: Photo) {
        database.photoDao().insertAll(PhotoEntityDb(photo))
    }

    fun clearDatabase(): Completable {
        return database.photoDao().deleteAll()
    }

}