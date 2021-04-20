package dan.nr.sample.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Save user info to data store.
 */
class UserPreferences(context: Context)
{
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    companion object
    {
        private val USER_NAME_KEY = preferencesKey<String>("user_name_key")
        private val USER_PASSWORD_KEY = preferencesKey<String>("user_password_key")
    }

    init
    {
        dataStore = applicationContext.createDataStore(name = "my_data_store")
    }

    val userName: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_NAME_KEY]
        }

    val userPassword: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_PASSWORD_KEY]
        }

    suspend fun saveUserInfo(username: String, password: String)
    {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] =username
            preferences[USER_PASSWORD_KEY] = password
        }
    }

    suspend fun clear()
    {
        dataStore.edit { preference ->
            preference.clear()
        }
    }

}