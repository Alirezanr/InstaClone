package dan.nr.sample.ui.base

import androidx.lifecycle.ViewModel
import dan.nr.sample.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel()
{
}