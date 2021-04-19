package dan.nr.sample.ui.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dan.nr.sample.R
import dan.nr.sample.databinding.FragmentPostsBinding
import dan.nr.sample.repository.PostsRepository
import dan.nr.sample.ui.base.BaseFragment

class PostsFragment : BaseFragment<PostsViewModel, FragmentPostsBinding, PostsRepository>()
{
    private val TAG = "!!!"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PostsRvAdapter()
        /*lifecycleScope.launchWhenStarted {
            try
            {
                viewModel.removeAllPosts()
                viewModel.addPost(Post(Date(),
                                       "Title",
                                       "Some Great Content",
                                       true,
                                       null,
                                       PostType.ONLY_TEXT))
                viewModel.addPost(Post(Date(),
                                       "Title 2 ",
                                       "Some Awesome Content",
                                       false,
                                       null,
                                       PostType.ONLY_TEXT))
            } catch (e: Exception)
            {
                Log.i(TAG, e.toString())
            }
        }*/

        viewModel.getPosts()
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.posts = it
            Log.i(TAG, it.size.toString())
            it.forEach {
                Log.i(TAG, it.title)
            }
        })




        binding.apply {
            rvPostsList.layoutManager = LinearLayoutManager(requireContext())
            rvPostsList.adapter = adapter

            fabCreateNewPost.setOnClickListener {
                findNavController().navigate(R.id.action_postsFragment_to_addPostFragment)
            }

        }
    }

    override fun getViewModel(): Class<PostsViewModel> = PostsViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater,
                                    container: ViewGroup?): FragmentPostsBinding = FragmentPostsBinding
            .inflate(inflater,
                     container,
                     false)

    override fun getFragmentRepository(): PostsRepository = PostsRepository(db.getSpendDao())

}
