package dan.nr.sample.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dan.nr.sample.R
import dan.nr.sample.util.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class SplashFragment : Fragment(R.layout.fragment_splash)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val userPreferences = UserPreferences(requireActivity())
        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main)
            {
                userPreferences.userName.collect {
                    delay(400)
                    if (it != null)
                    {
                        findNavController().navigate(R.id.action_splashFragment_to_postsFragment)
                    } else
                    {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}