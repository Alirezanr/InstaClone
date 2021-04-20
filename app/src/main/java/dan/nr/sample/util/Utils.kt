package dan.nr.sample.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.io.FileUtils
import java.io.File

fun View.isViewEnable(enabled: Boolean)
{
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.9f
}

fun View.viewVisibility(visibility: Int)
{
    when (visibility)
    {
        View.VISIBLE -> this.visibility = View.VISIBLE
        View.INVISIBLE -> this.visibility = View.INVISIBLE
        View.GONE -> this.visibility = View.GONE
    }
}

fun View.snackBar(message: String, action: (() -> Unit)? = null)
{
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

fun previewVideo(context: Context, filePath: String, videoView: VideoView)
{
    videoView.setVideoPath(filePath)
    val mediaController = MediaController(context)
    videoView.setMediaController(mediaController)
    mediaController.setMediaPlayer(videoView)
    videoView.visibility = View.VISIBLE
    videoView.start()
}

fun removeAllAppData()
{
    val dir = File(Environment.getExternalStorageDirectory().absolutePath + APP_PATH_SD_CARD)
    FileUtils.deleteDirectory(dir)
}