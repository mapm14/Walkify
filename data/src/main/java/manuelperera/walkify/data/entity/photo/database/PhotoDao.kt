package manuelperera.walkify.data.entity.photo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.walkify.data.datasource.local.PHOTO_ENTITY_TABLE_NAME

@Dao
interface PhotoDao {

    @Query("SELECT * FROM $PHOTO_ENTITY_TABLE_NAME ORDER BY addedDate DESC")
    fun getAll(): Observable<List<PhotoEntityDb>>

    @Query("SELECT * FROM $PHOTO_ENTITY_TABLE_NAME ORDER BY addedDate DESC LIMIT 1")
    fun getLastInserted(): PhotoEntityDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg photoDatabase: PhotoEntityDb)

    @Query("DELETE FROM $PHOTO_ENTITY_TABLE_NAME")
    fun deleteAll(): Completable

}