package dan.nr.sample.ui.posts

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dan.nr.sample.R
import dan.nr.sample.databinding.RvItemPostBinding
import dan.nr.sample.db.Post
import dan.nr.sample.model.PostType
import dan.nr.sample.util.viewVisibility

class PostsRvAdapter : RecyclerView.Adapter<PostsRvAdapter.PostViewHolder>()
{
    var posts: List<Post> = listOf()
        set(value)
        {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PostViewHolder(RvItemPostBinding.inflate(LayoutInflater.from(parent.context),
                                                     parent,
                                                     false))

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = with(holder.binding) {
        postTitle.text=posts[position].title
        postContent.text=posts[position].content
        if(posts[position].isLiked) postBtnLike.setImageResource(R.drawable.ic_red_heart)
        else postBtnLike.setImageResource(R.drawable.ic_heart_outline)

        when(posts[position].postType)
        {
            PostType.ONLY_TEXT->{
                postImage.viewVisibility(View.GONE)
                postVideo.viewVisibility(View.GONE)
            }
            PostType.IMAGE_AND_TEXT->{
                postImage.viewVisibility(View.VISIBLE)
                postVideo.viewVisibility(View.GONE)

                postImage.setImageURI(Uri.parse(posts[position].mediaUri))
            }
            PostType.VIDEO_AND_TEXT->{
                postImage.viewVisibility(View.GONE)
                postVideo.viewVisibility(View.VISIBLE)

                postVideo.setVideoURI(Uri.parse(posts[position].mediaUri))
            }
        }

        btnShare.setOnClickListener {
            Toast.makeText(it.context, "Toast", Toast.LENGTH_SHORT).show()
        }

    }
    inner class PostViewHolder(val binding: RvItemPostBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }
}