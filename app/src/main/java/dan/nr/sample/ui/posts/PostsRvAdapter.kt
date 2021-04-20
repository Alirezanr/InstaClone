package dan.nr.sample.ui.posts

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dan.nr.sample.R
import dan.nr.sample.databinding.RvItemPostBinding
import dan.nr.sample.db.Post
import dan.nr.sample.db.PostType
import dan.nr.sample.util.previewVideo
import dan.nr.sample.util.viewVisibility
import java.io.File

class PostsRvAdapter : RecyclerView.Adapter<PostsRvAdapter.PostViewHolder>()
{
    var posts: List<Post> = listOf()
        set(value)
        {
            field = value
            notifyDataSetChanged()
        }
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener
    {
        fun onLikeButtonClick(itemView: View, position: Int)
        fun onShareButtonClick(itemView: View, position: Int)
    }

    inner class PostViewHolder(val binding: RvItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: OnItemClickListener)
    {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PostViewHolder(RvItemPostBinding.inflate(LayoutInflater.from(parent.context),
                                                     parent,
                                                     false))

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = with(holder.binding) {
        postTitle.text = posts[position].title
        postContent.text = posts[position].content
        if (posts[position].isLiked) postBtnLike.setImageResource(R.drawable.ic_red_heart)
        else postBtnLike.setImageResource(R.drawable.ic_heart_outline)

        when (posts[position].postType)
        {
            PostType.ONLY_TEXT ->
            {
                postImage.viewVisibility(View.GONE)
                postVideo.viewVisibility(View.GONE)
            }
            PostType.IMAGE_AND_TEXT ->
            {
                postImage.viewVisibility(View.VISIBLE)
                postVideo.viewVisibility(View.GONE)

                val imgFile = File(posts[position].filePath!! )
                if (imgFile.exists())
                {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    postImage.setImageBitmap(myBitmap)
                } else
                    postImage.setImageResource(R.drawable.no_image)
            }
            PostType.VIDEO_AND_TEXT ->
            {
                postImage.viewVisibility(View.GONE)
                postVideo.viewVisibility(View.VISIBLE)

                previewVideo(this.root.context,posts[position].filePath!!,postVideo)
                postVideo.setVideoURI(Uri.parse(posts[position].filePath))
            }
        }

        btnShare.setOnClickListener {
            listener!!.onShareButtonClick(this.root,position)
        }
        postBtnLike.setOnClickListener {
            listener!!.onLikeButtonClick(this.root,position)
        }
    }
}