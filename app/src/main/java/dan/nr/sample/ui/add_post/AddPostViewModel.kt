package dan.nr.sample.ui.add_post

import androidx.lifecycle.viewModelScope
import dan.nr.sample.db.Post
import dan.nr.sample.repository.AddPostRepository
import dan.nr.sample.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AddPostViewModel(private val repository: AddPostRepository) : BaseViewModel(repository)
{
    fun addPost(post: Post)
    {
        viewModelScope.launch {
            repository.addPost(post)
        }
    }
}