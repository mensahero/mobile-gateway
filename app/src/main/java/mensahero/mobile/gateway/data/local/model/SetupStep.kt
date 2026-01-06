package mensahero.mobile.gateway.data.local.model

import java.net.URI

/**
 * Represents a single step in the setup process
 */
data class SetupStep(
    val id: Int, val title: String, val description: String = "", val isComplete: Boolean = false
) {
    companion object {
        const val STEP_WELCOME = 0

        const val STEP_SERVER = 1
        const val STEP_PROFILE = 2
        const val STEP_PREFERENCES = 3
        const val STEP_COMPLETE = 4

        fun getDefaultSteps(): List<SetupStep> = listOf(
            SetupStep(
                id = STEP_WELCOME, title = "Welcome", description = "Welcome to the app"
            ), SetupStep(
                id = STEP_SERVER, title = "Server Setup", description = "Set up your connection to server"
            ), SetupStep(
                id = STEP_PROFILE, title = "Profile Setup", description = "Set up your profile"
            ), SetupStep(
                id = STEP_PREFERENCES, title = "Preferences", description = "Configure your preferences"
            ), SetupStep(
                id = STEP_COMPLETE, title = "Complete", description = "Setup complete"
            )
        )
    }
}

/**
 * Holds user data collected during setup
 */
data class SetupUserData(
    val name: String = "",
    val email: String = "",
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
) {
    fun isProfileValid(): Boolean {
        return name.isNotBlank() && email.isNotBlank() && isValidEmail(email)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

data class SetupServerData(
    val apiServer: String = "",
    val websocketServer: String = "",
    val isConnectionTested: Boolean = false
) {
    fun isValid(): Boolean {
        return isApiServerValid() && isWebsocketValid() && isConnectionTested
    }

    fun isApiServerValid(): Boolean {
        val trimmed = apiServer.trim()
        if (trimmed.isBlank()) return false

        return try {
            val uri = URI(trimmed)
            val scheme = uri.scheme?.lowercase()
            val host = uri.host
            val port = uri.port

            // Check if scheme is http or https and host is present
            // Port is optional, but if present should be valid (1-65535)
            (scheme == "http" || scheme == "https") && !host.isNullOrBlank() && (port == -1 || port in 1..65535)
        } catch (e: Exception) {
            false
        }
    }

    fun isWebsocketValid(): Boolean {
        val trimmed = websocketServer.trim()
        if (trimmed.isBlank()) return false

        return try {
            val uri = URI(trimmed)
            val scheme = uri.scheme?.lowercase()
            val host = uri.host
            val port = uri.port

            // Check if scheme is ws or wss and host is present
            // Port is optional, but if present should be valid (1-65535)
            (scheme == "ws" || scheme == "wss") && !host.isNullOrBlank() && (port == -1 || port in 1..65535)
        } catch (e: Exception) {
            false
        }
    }
}