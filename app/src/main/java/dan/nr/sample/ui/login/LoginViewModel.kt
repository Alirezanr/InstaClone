package dan.nr.sample.ui.login

import dan.nr.sample.repository.LoginRepository
import dan.nr.sample.ui.base.BaseViewModel

class LoginViewModel(private val repository: LoginRepository): BaseViewModel(repository)
{
    suspend fun saveUserInfo(username:String,password:String)
    {
        repository.saveUserInfo(username,password)
    }
}