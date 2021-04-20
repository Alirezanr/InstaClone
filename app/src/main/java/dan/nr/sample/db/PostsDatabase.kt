package dan.nr.sample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Post::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class,PostTypeConverter::class)
abstract class PostsDatabase : RoomDatabase()
{

    abstract fun getPostsDao(): PostDao

    companion object
    {
        private const val DB_NAME = "Posts-Database.db"

        @Volatile
        private var instance: PostsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                                     PostsDatabase::class.java,
                                     DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
    }
}