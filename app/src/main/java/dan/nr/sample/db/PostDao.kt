package dan.nr.sample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao
{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(post: Post)

    @Query("DELETE FROM posts")
    suspend fun removeAllPosts()

    @Query("SELECT * FROM posts ORDER BY date DESC")
    fun getPosts(): Flow<List<Post>>

}