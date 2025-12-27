package mensahero.mobile.gateway.presentation.setup

sealed class SetupEvent {
    data object NextStep : SetupEvent()
    data object PreviousStep : SetupEvent()
    data class JumpToStep(val index: Int) : SetupEvent()
    data object CompleteSetup : SetupEvent()

    // User data updates
    data class UpdateUserName(val name: String) : SetupEvent()
    data class UpdateUserEmail(val email: String) : SetupEvent()
    data class UpdateNotificationEnabled(val enabled: Boolean) : SetupEvent()
    data class UpdateDarkModeEnabled(val enabled: Boolean) : SetupEvent()

    // Error handling
    data object ClearError : SetupEvent()
    data object ResetSetup : SetupEvent()
}