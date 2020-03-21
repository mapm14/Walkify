package manuelperera.walkify.data.datasource.local.converters

import androidx.room.TypeConverter
import java.util.*

class TimestampConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

}