package dan.nr.sample.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dan.nr.sample.db.PostsDatabase
import dan.nr.sample.repository.BaseRepository
import dan.nr.sample.util.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment()
{
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected lateinit var db: PostsDatabase

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        try
        {
            userPreferences = UserPreferences(requireContext())
            binding = getFragmentBinding(inflater, container)
            db = PostsDatabase(requireContext())
            val factory = ViewModelFactory(getFragmentRepository())
            viewModel = ViewModelProvider(this, factory).get(getViewModel())

            lifecycleScope.launch {
                userPreferences.userName
                userPreferences.userPassword
            }
        } catch (e: Exception)
        {
            Log.i("!!!", e.toString())
        }
        return binding.root
    }

    fun logout() = lifecycleScope.launch {
        userPreferences.clear()
        findNavController().navigate(dan.nr.sample.R.id.action_postsFragment_to_loginFragment)
    }

    fun getUserInfo(): LiveData<String?>
    {
        return userPreferences.userName.asLiveData()
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun getFragmentRepository(): R
}