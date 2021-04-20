package dan.nr.sample.db

import androidx.room.TypeConverter
import java.util.*

/**
 * Helper class.
 * Converts Date to timestamp and timestamp to Date
 */
class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}