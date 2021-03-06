package dan.nr.sample.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "posts")
data class Post(val date: Date,
                @ColumnInfo(name = "title")
                val title: String,
                @ColumnInfo(name = "content")
                val content: String = "",
                @ColumnInfo(name = "is_liked")
                val isLiked: Boolean,
                @ColumnInfo(name = "media_uri")
                val filePath: String?=null,
                @ColumnInfo(name = "post_type")
                @TypeConverters(PostTypeConverter::class)
                val postType: PostType)
{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}


