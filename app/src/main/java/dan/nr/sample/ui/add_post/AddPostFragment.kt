package dan.nr.sample.ui.add_post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dan.nr.sample.R
import dan.nr.sample.databinding.FragmentAddPostBinding
import dan.nr.sample.db.Post
import dan.nr.sample.db.PostsDatabase
import dan.nr.sample.repository.PostsRepository
import kotlinx.coroutines.flow.forEach
import java.util.*

class AddPostFragment : Fragment()
{

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}