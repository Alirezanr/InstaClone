package dan.nr.sample.ui.base

import androidx.lifecycle.ViewModel
import dan.nr.sample.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel()
{
    suspend fun logout() = withContext(Dispatchers.IO) {
        repository.logout()
    }
}