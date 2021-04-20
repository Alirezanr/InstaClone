package dan.nr.sample.db

import androidx.room.TypeConverter

/**
 * Helper class.
 * Converts PostType to int and int to PostType
 */
class PostTypeConverter
{

    @TypeConverter
    fun fromPostType(value: PostType): Int
    {
        return value.ordinal
    }

    @TypeConverter
    fun toPostType(value: Int): PostType
    {
        return when (value)
        {
            0 -> PostType.ONLY_TEXT
            1 -> PostType.IMAGE_AND_TEXT
            2 -> PostType.VIDEO_AND_TEXT
            else -> PostType.ONLY_TEXT
        }
    }
}