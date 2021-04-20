package dan.nr.sample.ui.posts

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dan.nr.sample.R
import dan.nr.sample.databinding.FragmentPostsBinding
import dan.nr.sample.db.Post
import dan.nr.sample.repository.PostsRepository
import dan.nr.sample.ui.base.BaseFragment
import dan.nr.sample.util.removeAllAppData
import dan.nr.sample.util.snackBar

class PostsFragment : BaseFragment<PostsViewModel, FragmentPostsBinding, PostsRepository>(),
                      PostsRvAdapter.OnItemClickListener
{
    private val TAG = "!!!"
    private var postsList: List<Post> = ArrayList<Post>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        //RecyclerView adapter
        val adapter = PostsRvAdapter()
        adapter.setOnItemClickListener(this)


        viewModel.getPosts()
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            postsList = it
            adapter.posts = postsList
        })

        binding.apply {
            rvPostsList.layoutManager = LinearLayoutManager(requireContext())
            rvPostsList.adapter = adapter
            rvPostsList.addItemDecoration(DividerItemDecoration(requireContext(),
                                                                LinearLayoutManager.VERTICAL))

            //Set click listener to floating action button
            fabCreateNewPost.setOnClickListener {
                findNavController().navigate(R.id.action_postsFragment_to_addPostFragment)
            }

            getUserInfo().observe(viewLifecycleOwner, Observer {
                txtUsername.text = "Welcome $it"
            })

        }
    }

    override fun onLikeButtonClick(itemView: View, position: Int)
    {
        viewModel.updatePost(!(postsList[position].isLiked), postsList[position].id)
    }

    override fun onShareButtonClick(itemView: View, position: Int)
    {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
                     "title : ${postsList[position].title}\ncontent : ${postsList[position].content}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.menuItemRemoveAllPosts ->
            {
                viewModel.removeAllPosts()
                requireView().snackBar("All Posts Removed")
                removeAllAppData()
            }
            R.id.menuItemLogout ->
            {
                viewModel.removeAllPosts()
                logout()
                removeAllAppData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewModel(): Class<PostsViewModel> = PostsViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater,
                                    container: ViewGroup?): FragmentPostsBinding = FragmentPostsBinding
            .inflate(inflater,
                     container,
                     false)

    override fun getFragmentRepository(): PostsRepository = PostsRepository(db.getPostsDao())


}
