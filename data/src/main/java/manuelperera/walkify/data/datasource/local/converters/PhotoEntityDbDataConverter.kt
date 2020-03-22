package manuelperera.walkify.data.datasource.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import manuelperera.walkify.data.entity.photo.database.PhotoEntityDb

class PhotoEntityDbDataConverter {

    private val gson = Gson()
    private val type = object : TypeToken<List<PhotoEntityDb.SizeInfo>>() {}.type

    @TypeConverter
    fun toJson(value: List<PhotoEntityDb.SizeInfo>): String = gson.toJson(value, type)

    @TypeConverter
    fun toList(value: String): List<PhotoEntityDb.SizeInfo> = gson.fromJson(value, type)

}