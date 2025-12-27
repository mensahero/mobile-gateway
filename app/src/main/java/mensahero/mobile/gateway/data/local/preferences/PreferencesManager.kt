package mensahero.mobile.gateway.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Manages SharedPreferences operations with coroutine support
 * Thread-safe and follows MVVM best practices
 * Uses "app_prefs" to match your existing MainActivity preferences
 */
class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "app_prefs", // Matches your MainActivity
        Context.MODE_PRIVATE
    )

    // Setup Status
    suspend fun isSetupCompleted(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(PreferencesKeys.KEY_SETUP_COMPLETED, false)
    }

    suspend fun setSetupCompleted(completed: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit().apply {
            putBoolean(PreferencesKeys.KEY_SETUP_COMPLETED, completed)
            if (completed) {
                putLong(PreferencesKeys.KEY_SETUP_COMPLETION_TIMESTAMP, System.currentTimeMillis())
            }
            apply()
        }
    }

    // User Data
    suspend fun saveUserName(name: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(PreferencesKeys.KEY_USER_NAME, name)
            .apply()
    }

    suspend fun getUserName(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(PreferencesKeys.KEY_USER_NAME, null)
    }

    suspend fun saveUserEmail(email: String) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(PreferencesKeys.KEY_USER_EMAIL, email)
            .apply()
    }

    suspend fun getUserEmail(): String? = withContext(Dispatchers.IO) {
        sharedPreferences.getString(PreferencesKeys.KEY_USER_EMAIL, null)
    }

    // Preferences
    suspend fun saveNotificationEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putBoolean(PreferencesKeys.KEY_NOTIFICATION_ENABLED, enabled)
            .apply()
    }

    suspend fun isNotificationEnabled(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(PreferencesKeys.KEY_NOTIFICATION_ENABLED, true)
    }

    suspend fun saveDarkModeEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putBoolean(PreferencesKeys.KEY_DARK_MODE_ENABLED, enabled)
            .apply()
    }

    suspend fun isDarkModeEnabled(): Boolean = withContext(Dispatchers.IO) {
        sharedPreferences.getBoolean(PreferencesKeys.KEY_DARK_MODE_ENABLED, false)
    }

    // Utility
    suspend fun clearAllPreferences() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().clear().apply()
    }

    suspend fun getSetupCompletionTimestamp(): Long = withContext(Dispatchers.IO) {
        sharedPreferences.getLong(PreferencesKeys.KEY_SETUP_COMPLETION_TIMESTAMP, 0L)
    }
}