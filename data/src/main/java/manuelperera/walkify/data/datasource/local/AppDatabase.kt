package manuelperera.walkify.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import manuelperera.walkify.data.datasource.local.converters.PhotoEntityDbDataConverter
import manuelperera.walkify.data.datasource.local.converters.TimestampConverter
import manuelperera.walkify.data.entity.photo.database.PhotoDao
import manuelperera.walkify.data.entity.photo.database.PhotoEntityDb

const val PHOTO_ENTITY_TABLE_NAME = "PHOTO_ENTITY_TABLE_NAME"

@Database(entities = [PhotoEntityDb::class], version = 1, exportSchema = false)
@TypeConverters(value = [PhotoEntityDbDataConverter::class, TimestampConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
}