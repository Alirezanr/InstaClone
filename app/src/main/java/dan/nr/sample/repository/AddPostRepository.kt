package dan.nr.sample.repository

import dan.nr.sample.db.Post
import dan.nr.sample.db.PostDao

class AddPostRepository(private val dao: PostDao) : BaseRepository()
{
    suspend fun addPost(post: Post) = dao.addPost(post)
}