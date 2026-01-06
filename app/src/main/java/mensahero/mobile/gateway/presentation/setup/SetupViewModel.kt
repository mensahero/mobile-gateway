package mensahero.mobile.gateway.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import mensahero.mobile.gateway.data.local.model.SetupStep
import mensahero.mobile.gateway.domain.usecase.CompleteSetupUseCase
import mensahero.mobile.gateway.domain.usecase.TestServerConnectionUseCase
import mensahero.mobile.gateway.domain.usecase.TestWebSocketConnectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Setup screen with Hilt dependency injection
 * Manages UI state and business logic for the initial setup flow
 * Now includes server connection testing
 */
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val completeSetupUseCase: CompleteSetupUseCase,
    private val testServerConnectionUseCase: TestServerConnectionUseCase,
    private val testWebSocketConnectionUseCase: TestWebSocketConnectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SetupState())
    val state: StateFlow<SetupState> = _state.asStateFlow()

    /**
     * Handle user events
     */
    fun onEvent(event: SetupEvent) {
        when (event) {
            is SetupEvent.NextStep -> moveToNextStep()
            is SetupEvent.PreviousStep -> moveToPreviousStep()
            is SetupEvent.JumpToStep -> jumpToStep(event.index)
            is SetupEvent.CompleteSetup -> completeSetup()
            is SetupEvent.UpdateUserName -> updateUserName(event.name)
            is SetupEvent.UpdateUserEmail -> updateUserEmail(event.email)
            is SetupEvent.UpdateNotificationEnabled -> updateNotificationEnabled(event.enabled)
            is SetupEvent.UpdateDarkModeEnabled -> updateDarkModeEnabled(event.enabled)
            is SetupEvent.UpdateApiServer -> updateApiServer(event.server)
            is SetupEvent.UpdateWebsocketServer -> updateWebSocketServer(event.server)
            is SetupEvent.TestServerConnection -> testServerConnection()
            is SetupEvent.ClearError -> clearError()
            is SetupEvent.ResetSetup -> resetSetup()
        }
    }

    private fun moveToNextStep() {
        _state.update { currentState ->
            if (!validateCurrentStep(currentState)) {
                return@update currentState.copy(
                    error = getValidationError(currentState.currentStepIndex)
                )
            }

            val nextIndex = (currentState.currentStepIndex + 1)
                .coerceAtMost(currentState.steps.size - 1)

            val updatedSteps = currentState.steps.mapIndexed { index, step ->
                if (index == currentState.currentStepIndex) {
                    step.copy(isComplete = true)
                } else {
                    step
                }
            }

            currentState.copy(
                currentStepIndex = nextIndex,
                steps = updatedSteps,
                error = null,
                connectionTestResult = null,
                canNavigateBack = nextIndex > 0,
                canNavigateNext = nextIndex < currentState.steps.size - 1
            )
        }
    }

    private fun moveToPreviousStep() {
        _state.update { currentState ->
            val previousIndex = (currentState.currentStepIndex - 1).coerceAtLeast(0)
            currentState.copy(
                currentStepIndex = previousIndex,
                error = null,
                connectionTestResult = null,
                canNavigateBack = previousIndex > 0,
                canNavigateNext = true
            )
        }
    }

    private fun jumpToStep(index: Int) {
        _state.update { currentState ->
            val validIndex = index.coerceIn(0, currentState.steps.size - 1)
            currentState.copy(
                currentStepIndex = validIndex,
                error = null,
                connectionTestResult = null,
                canNavigateBack = validIndex > 0,
                canNavigateNext = validIndex < currentState.steps.size - 1
            )
        }
    }

    private fun testServerConnection() {
        val currentState = _state.value

        // Validate server URLs first
        if (currentState.serverData.apiServer.isBlank()) {
            _state.update { it.copy(error = "Please enter API server URL") }
            return
        }

        if (currentState.serverData.websocketServer.isBlank()) {
            _state.update { it.copy(error = "Please enter WebSocket server URL") }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isTestingConnection = true,
                    error = null,
                    connectionTestResult = null
                )
            }

            try {
                // Test API server
                val apiResult = testServerConnectionUseCase(currentState.serverData.apiServer)

                if (apiResult.isFailure) {
                    _state.update {
                        it.copy(
                            isTestingConnection = false,
                            error = "API Server: ${apiResult.exceptionOrNull()?.message ?: "Connection failed"}",
                            connectionTestResult = "Failed"
                        )
                    }
                    return@launch
                }

                // Test WebSocket server
                val wsResult = testWebSocketConnectionUseCase(currentState.serverData.websocketServer)

                if (wsResult.isFailure) {
                    _state.update {
                        it.copy(
                            isTestingConnection = false,
                            error = "WebSocket Server: ${wsResult.exceptionOrNull()?.message ?: "Connection failed"}",
                            connectionTestResult = "Failed"
                        )
                    }
                    return@launch
                }

                // Both successful
                val apiTestResult = apiResult.getOrNull()
                val wsTestResult = wsResult.getOrNull()

                val successMessage = buildString {
                    append("✓ API Server: ${apiTestResult?.message ?: "Connected"}")
                    if (apiTestResult?.responseTime != null) {
                        append(" (${apiTestResult.responseTime}ms)")
                    }
                    append("\n")
                    append("✓ WebSocket: ${wsTestResult?.message ?: "Connected"}")
                    if (wsTestResult?.responseTime != null) {
                        append(" (${wsTestResult.responseTime}ms)")
                    }
                }

                _state.update {
                    it.copy(
                        isTestingConnection = false,
                        connectionTestResult = successMessage,
                        serverData = it.serverData.copy(isConnectionTested = true),
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isTestingConnection = false,
                        error = "Connection test failed: ${e.message}",
                        connectionTestResult = "Failed"
                    )
                }
            }
        }
    }

    private fun completeSetup() {
        val currentState = _state.value

        if (!currentState.userData.isProfileValid()) {
            _state.update { it.copy(error = "Please complete all required fields") }
            return
        }

        if (!currentState.serverData.isValid()) {
            _state.update { it.copy(error = "Please configure and test server connection") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = completeSetupUseCase(
                currentState.userData,
                currentState.serverData
            )

            _state.update { state ->
                if (result.isSuccess) {
                    state.copy(
                        isLoading = false,
                        isSetupComplete = true,
                        error = null
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to complete setup"
                    )
                }
            }
        }
    }

    private fun validateCurrentStep(state: SetupState): Boolean {
        return when (state.currentStepIndex) {
            SetupStep.STEP_WELCOME -> true
            SetupStep.STEP_PROFILE -> state.userData.isProfileValid()
            SetupStep.STEP_SERVER -> state.serverData.isValid() && state.serverData.isConnectionTested
            SetupStep.STEP_PREFERENCES -> true
            SetupStep.STEP_COMPLETE -> true
            else -> false
        }
    }

    private fun getValidationError(stepIndex: Int): String {
        return when (stepIndex) {
            SetupStep.STEP_PROFILE -> "Please enter valid name and email"
            SetupStep.STEP_SERVER -> "Please configure and test server connection"
            else -> "Please complete all required fields"
        }
    }

    private fun updateUserName(name: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(name = name),
                error = null
            )
        }
    }

    private fun updateUserEmail(email: String) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(email = email),
                error = null
            )
        }
    }

    private fun updateNotificationEnabled(enabled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(notificationsEnabled = enabled)
            )
        }
    }

    private fun updateDarkModeEnabled(enabled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                userData = currentState.userData.copy(darkModeEnabled = enabled)
            )
        }
    }

    private fun updateApiServer(server: String) {
        _state.update { currentState ->
            currentState.copy(
                serverData = currentState.serverData.copy(
                    apiServer = server,
                    isConnectionTested = false
                ),
                error = null,
                connectionTestResult = null
            )
        }
    }

    private fun updateWebSocketServer(server: String) {
        _state.update { currentState ->
            currentState.copy(
                serverData = currentState.serverData.copy(
                    websocketServer = server,
                    isConnectionTested = false
                ),
                error = null,
                connectionTestResult = null
            )
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null, connectionTestResult = null) }
    }

    private fun resetSetup() {
        _state.value = SetupState()
    }
}