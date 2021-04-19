package dan.nr.sample.repository

import dan.nr.sample.util.UserPreferences

class LoginRepository( private val preferences: UserPreferences):BaseRepository()
{
    suspend fun saveUserInfo(username: String, password: String)
    {
        preferences.saveUserInfo(username,password)
    }
}