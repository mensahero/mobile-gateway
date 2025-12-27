package mensahero.mobile.gateway.data.local.model

/**
 * Represents a single step in the setup process
 */
data class SetupStep(
    val id: Int,
    val title: String,
    val description: String = "",
    val isComplete: Boolean = false
) {
    companion object {
        const val STEP_WELCOME = 0
        const val STEP_PROFILE = 1
        const val STEP_PREFERENCES = 2
        const val STEP_COMPLETE = 3

        fun getDefaultSteps(): List<SetupStep> = listOf(
            SetupStep(
                id = STEP_WELCOME,
                title = "Welcome",
                description = "Welcome to the app"
            ),
            SetupStep(
                id = STEP_PROFILE,
                title = "Profile Setup",
                description = "Set up your profile"
            ),
            SetupStep(
                id = STEP_PREFERENCES,
                title = "Preferences",
                description = "Configure your preferences"
            ),
            SetupStep(
                id = STEP_COMPLETE,
                title = "Complete",
                description = "Setup complete"
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