package mensahero.mobile.gateway.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import mensahero.mobile.gateway.data.local.model.SetupStep
import mensahero.mobile.gateway.domain.usecase.CompleteSetupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Setup screen with Hilt dependency injection
 * Manages UI state and business logic for the initial setup flow
 */
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val completeSetupUseCase: CompleteSetupUseCase
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
            is SetupEvent.ClearError -> clearError()
            is SetupEvent.ResetSetup -> resetSetup()
        }
    }

    private fun moveToNextStep() {
        _state.update { currentState ->
            if (!validateCurrentStep(currentState)) {
                return@update currentState.copy(
                    error = "Please complete all required fields"
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
                canNavigateBack = validIndex > 0,
                canNavigateNext = validIndex < currentState.steps.size - 1
            )
        }
    }

    private fun completeSetup() {
        val currentState = _state.value

        if (!currentState.userData.isProfileValid()) {
            _state.update { it.copy(error = "Please complete all required fields") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = completeSetupUseCase(currentState.userData)

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
            SetupStep.STEP_PREFERENCES -> true
            SetupStep.STEP_COMPLETE -> true
            else -> false
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

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun resetSetup() {
        _state.value = SetupState()
    }
}