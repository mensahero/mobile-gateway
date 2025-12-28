package mensahero.mobile.gateway.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mensahero.mobile.gateway.data.local.model.SetupServerData
import mensahero.mobile.gateway.data.local.model.SetupUserData
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager

/**
 * Repository pattern implementation for setup-related operations
 * Single source of truth for setup data
 */
class SetupRepository(
    private val preferencesManager: PreferencesManager
) {

    /**
     * Check if the initial setup has been completed
     */
    suspend fun isSetupCompleted(): Boolean {
        return preferencesManager.isSetupCompleted()
    }

    /**
     * Mark setup as completed and save user data
     */
    suspend fun completeSetup(userData: SetupUserData, serverData: SetupServerData): Result<Unit> {
        return try {
            preferencesManager.saveUserName(userData.name)
            preferencesManager.saveUserEmail(userData.email)
            preferencesManager.saveApiServer(serverData.apiServer)
            preferencesManager.saveWebsocketServer(serverData.websocketServer)
            preferencesManager.saveNotificationEnabled(userData.notificationsEnabled)
            preferencesManager.saveDarkModeEnabled(userData.darkModeEnabled)
            preferencesManager.setSetupCompleted(true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reset setup status (for testing or reset functionality)
     */
    suspend fun resetSetup(): Result<Unit> {
        return try {
            preferencesManager.clearAllPreferences()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get stored user data
     */
    suspend fun getUserData(): SetupUserData {
        return SetupUserData(
            name = preferencesManager.getUserName() ?: "",
            email = preferencesManager.getUserEmail() ?: "",
            notificationsEnabled = preferencesManager.isNotificationEnabled(),
            darkModeEnabled = preferencesManager.isDarkModeEnabled()
        )
    }

    suspend fun getServerData(): SetupServerData {
        return SetupServerData(
            apiServer = preferencesManager.getApiServer() ?: "",
            websocketServer = preferencesManager.getWebsocketServer() ?: "",
        )
    }

    /**
     * Observe setup completion status
     */
    fun observeSetupStatus(): Flow<Boolean> = flow {
        emit(preferencesManager.isSetupCompleted())
    }
}
