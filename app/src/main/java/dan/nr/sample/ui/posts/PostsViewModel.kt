package dan.nr.sample.ui.posts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dan.nr.sample.db.Post
import dan.nr.sample.repository.PostsRepository
import dan.nr.sample.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsViewModel(private val repository: PostsRepository) : BaseViewModel(repository)
{
    private val TAG = "!!!"
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    suspend fun addPost(post: Post) = repository.addPost(post)

    suspend fun removeAllPosts() = repository.removeAllPosts()

    fun getPosts()
    {
        Log.i(TAG, "getPosts()j")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getPostsFlow().collect {
                    withContext(Dispatchers.Main)
                    {
                        Log.d(TAG, "setting value on = " + Thread.currentThread().name)

                        _posts.value = it

                    }
                }

            }
        }
    }
}