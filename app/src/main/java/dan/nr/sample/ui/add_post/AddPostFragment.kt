package dan.nr.sample.ui.add_post

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import dan.nr.sample.R
import dan.nr.sample.databinding.FragmentAddPostBinding
import dan.nr.sample.db.Post
import dan.nr.sample.db.PostType
import dan.nr.sample.repository.AddPostRepository
import dan.nr.sample.ui.base.BaseFragment
import dan.nr.sample.util.*
import dan.nr.sample.util.InputValidator.validateInputs
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class AddPostFragment : BaseFragment<AddPostViewModel,
        FragmentAddPostBinding,
        AddPostRepository>(),
                        EasyPermissions.PermissionCallbacks
{
    private val TAG = "!!!"
    private val SELECT_PHOTO = 1
    private val RC_STORAGE_PERM: Int = 123

    private var filePath: String = ""
    private var postType: PostType = PostType.ONLY_TEXT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addPostFragmentBtnPost.isViewEnable(false)

            btnMediaAddPost.setOnClickListener {
                checkPermission()
            }

            addPostFragmentBtnPost.setOnClickListener {
                addNewPost()
                findNavController().navigate(R.id.action_addPostFragment_to_postsFragment)
            }

            addPostFragmentEdtTitle.addTextChangedListener {
                if (validateInputs(addPostFragmentEdtTitle.text.toString().trim(),
                                   addPostFragmentEdtContent.text.toString().trim()))
                {
                    addPostFragmentBtnPost.isViewEnable(true)
                } else
                {
                    addPostFragmentBtnPost.isViewEnable(false)
                }
            }
            addPostFragmentEdtContent.addTextChangedListener {
                if (validateInputs(addPostFragmentEdtTitle.text.toString().trim(),
                                   addPostFragmentEdtContent.text.toString().trim()))
                {
                    addPostFragmentBtnPost.isViewEnable(true)
                } else
                {
                    addPostFragmentBtnPost.isViewEnable(false)
                }
            }
        }
    }

    @AfterPermissionGranted(123)
    private fun checkPermission()
    {
        val perms = Manifest.permission.WRITE_EXTERNAL_STORAGE

        if (EasyPermissions.hasPermissions(requireContext(),
                                           perms))
        {
            openGallery()
        } else
        {
            EasyPermissions.requestPermissions(this,
                                               ACCESS_TO_STORAGE,
                                               RC_STORAGE_PERM,
                                               perms);
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK)
        {
            val imagesUri = Matisse.obtainResult(data)
            val fileExtension = getFileNameFromUri(imagesUri.first()).split(".").last()
            when (fileExtension)
            {
                "mp4" ->
                {
                    saveVideoAndShowPreview(imagesUri.first())
                    Log.i(TAG, "onActivityResult: video ")
                }
                "jpg", "png", "jpeg" ->
                {
                    saveImageAndShowPreview(imagesUri.first())
                    Log.i(TAG, "onActivityResult: image ")
                }
                else ->
                {
                    requireView().snackBar("This type of file not supported.")
                }
            }
        }
    }

    private fun saveVideoAndShowPreview(fileUri: Uri)
    {
        binding.addPostFragmentVideoView.viewVisibility(View.VISIBLE)

        postType = copyVideoToDirecrory(requireContext(), fileUri)
        previewVideo(requireContext(), filePath, binding.addPostFragmentVideoView)
    }

    private fun openGallery()
    {
        binding.addPostFragmentVideoView.viewVisibility(View.GONE)
        binding.addPostFragmentImageView.viewVisibility(View.GONE)

        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .showPreview(true) // Default is `true`
                .forResult(SELECT_PHOTO);
    }

    fun getFileNameFromUri(uri: Uri): String
    {
        var result: String? = null
        if (uri.scheme == "content")
        {
            val cursor: Cursor? =
                    requireActivity().getContentResolver().query(uri, null, null, null, null)
            try
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally
            {
                cursor!!.close()
            }
        }
        if (result == null)
        {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1)
            {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun saveImageAndShowPreview(uri: Uri)
    {
        binding.addPostFragmentImageView.viewVisibility(View.VISIBLE)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        binding.addPostFragmentImageView.setImageBitmap(bitmap)

        postType = saveImageToStorage(bitmap)
    }

    private fun saveImageToStorage(image: Bitmap): PostType
    {
        val fullPath =
                Environment.getExternalStorageDirectory().absolutePath + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD
        try
        {
            val dir = File(fullPath)
            if (!dir.exists())
            {
                dir.mkdirs()
            }
            var fOut: OutputStream? = null
            val fileName = "JPEG_" + System.currentTimeMillis() + ".png"
            val file = File(fullPath, fileName)
            file.createNewFile()
            fOut = FileOutputStream(file)

            // 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            MediaStore.Images.Media.insertImage(requireContext().contentResolver,
                                                file.absolutePath,
                                                file.name,
                                                file.name)
            filePath = file.absolutePath
            return PostType.IMAGE_AND_TEXT
        } catch (e: Exception)
        {
            Log.e("saveToExternalStorage()", e.message!!)
            return PostType.ONLY_TEXT
        }
    }

    private fun copyVideoToDirecrory(context: Context, fileUri: Uri?): PostType
    {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try
        {
            val content: ContentResolver = context.getContentResolver()
            inputStream = content.openInputStream(fileUri!!)
            val root = Environment.getExternalStorageDirectory()
            if (root == null)
            {
                Log.d(TAG, "Failed to get root")
            }
            // create a directory
            val saveDirectory =
                    File(Environment.getExternalStorageDirectory().absolutePath + APP_PATH_SD_CARD + APP_VIDEOS_PATH_SD_CARD)
            // create direcotory if it doesn't exists
            saveDirectory.mkdirs()
            val fileName = "VID_" + System.currentTimeMillis() + ".mp4"
            val file = File(saveDirectory, fileName)

            outputStream = FileOutputStream(file)
            if (outputStream != null)
            {
                Log.e(TAG, "Output Stream Opened successfully")
            }
            val buffer = ByteArray(1000)
            var bytesRead = 0
            while (inputStream!!.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0)
            {
                outputStream.write(buffer, 0, buffer.size)
            }
            filePath = file.absolutePath
            return PostType.VIDEO_AND_TEXT
        } catch (e: java.lang.Exception)
        {
            Log.e(TAG, "Exception occurred " + e.message)
            return PostType.ONLY_TEXT
        }
    }

    private fun addNewPost()
    {
        val title = binding.addPostFragmentEdtTitle.text.toString().trim()
        val content = binding.addPostFragmentEdtContent.text.toString().trim()

        viewModel.addPost(Post(Date(),
                               title,
                               content,
                               false,
                               filePath,
                               postType))
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>)
    {
        openGallery()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>)
    {

    }

    override fun getViewModel(): Class<AddPostViewModel> = AddPostViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater,
                                    container: ViewGroup?): FragmentAddPostBinding =
            FragmentAddPostBinding.inflate(inflater,
                                           container,
                                           false)

    override fun getFragmentRepository(): AddPostRepository = AddPostRepository(db.getPostsDao())

}