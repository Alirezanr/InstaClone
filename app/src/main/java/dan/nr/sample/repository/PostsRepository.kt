package dan.nr.sample.repository

import dan.nr.sample.db.Post
import dan.nr.sample.db.PostDao
import kotlinx.coroutines.flow.Flow

class PostsRepository(private val dao: PostDao) : BaseRepository()
{
    private val TAG = "!!!"
    suspend fun addPost(post: Post) = dao.addPost(post)
    suspend fun removeAllPosts() = dao.removeAllPosts()

    fun getPostsFlow(): Flow<List<Post>> = dao.getPosts()

}