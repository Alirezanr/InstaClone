package dan.nr.sample.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dan.nr.sample.repository.AddPostRepository
import dan.nr.sample.repository.BaseRepository
import dan.nr.sample.repository.LoginRepository
import dan.nr.sample.repository.PostsRepository
import dan.nr.sample.ui.add_post.AddPostViewModel
import dan.nr.sample.ui.login.LoginViewModel
import dan.nr.sample.ui.posts.PostsViewModel

class ViewModelFactory(private val repository: BaseRepository) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        return when
        {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            {
                LoginViewModel(repository = repository as LoginRepository) as T
            }
            modelClass.isAssignableFrom(PostsViewModel::class.java) ->
            {
                PostsViewModel(repository = repository as PostsRepository) as T
            }
            modelClass.isAssignableFrom(AddPostViewModel::class.java) ->
            {
                AddPostViewModel(repository = repository as AddPostRepository) as T
            }
            else ->
            {
                throw IllegalArgumentException("ViewModel class not found")
            }
        }
    }
}