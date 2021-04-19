package dan.nr.sample.util

import android.app.Activity
import android.content.Intent
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun <A : Activity> Activity.startNewActivity(activity: Class<A>)
{
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.setViewVisibility(isVisible: Boolean)
{
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.isViewEnable(enabled: Boolean)
{
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.9f
}
fun View.viewVisibility(visibility: Int)
{
    when(visibility)
    {
        View.VISIBLE->this.visibility=View.VISIBLE
        View.INVISIBLE->this.visibility=View.INVISIBLE
        View.GONE->this.visibility=View.GONE
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

