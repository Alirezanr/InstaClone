package dan.nr.sample.ui.posts

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
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    /**
     * Update post's like status
     */
    fun updatePost(isLiked: Boolean, id: Long)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updatePost(isLiked, id)
            }
        }
    }

    fun removeAllPosts()
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.removeAllPosts()
            }
        }
    }

    fun getPosts()
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getPosts().collect {
                    withContext(Dispatchers.Main)
                    {
                        _posts.value = it
                    }
                }

            }
        }
    }
}