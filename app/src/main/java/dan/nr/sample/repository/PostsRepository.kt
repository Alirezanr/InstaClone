package dan.nr.sample.repository

import dan.nr.sample.db.Post
import dan.nr.sample.db.PostDao
import kotlinx.coroutines.flow.Flow

class PostsRepository(private val dao: PostDao) : BaseRepository()
{
    suspend fun removeAllPosts() = dao.removeAllPosts()

    suspend fun updatePost(isLiked: Boolean, id: Long) = dao.updatePost(isLiked , id )

    fun getPosts(): Flow<List<Post>> = dao.getPosts()
}