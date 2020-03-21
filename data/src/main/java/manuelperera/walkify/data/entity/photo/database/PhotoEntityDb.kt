package manuelperera.walkify.data.entity.photo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import manuelperera.walkify.data.datasource.local.PHOTO_ENTITY_TABLE_NAME
import manuelperera.walkify.data.datasource.local.converters.PhotoEntityDbDataConverter
import manuelperera.walkify.data.datasource.local.converters.TimestampConverter
import manuelperera.walkify.data.entity.base.DataObject
import manuelperera.walkify.data.extensions.toEnum
import manuelperera.walkify.domain.entity.photo.Photo
import java.util.*

@Entity(tableName = PHOTO_ENTITY_TABLE_NAME)
class PhotoEntityDb(
    @PrimaryKey val id: String,
    @TypeConverters(PhotoEntityDbDataConverter::class) val sizeInfoList: List<SizeInfo>,
    @TypeConverters(TimestampConverter::class) val addedDate: Date
) : DataObject<Photo> {

    constructor(photo: Photo) : this(
        id = photo.id,
        sizeInfoList = photo.sizeList.map { SizeInfo(it.label.name, it.url) },
        addedDate = Date()
    )

    class SizeInfo(
        val label: String,
        val url: String
    )

    override fun toDomain(): Photo = Photo(id, sizeInfoList.map { Photo.SizeInfo(it.label.toEnum(), it.url) })

}