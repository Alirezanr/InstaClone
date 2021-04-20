package dan.nr.sample.ui.login

import androidx.lifecycle.viewModelScope
import dan.nr.sample.repository.LoginRepository
import dan.nr.sample.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository): BaseViewModel(repository)
{
     fun saveUserInfo(username:String,password:String)
    {
        viewModelScope.launch {
            repository.saveUserInfo(username,password)
        }
    }
}