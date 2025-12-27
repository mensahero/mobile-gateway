package mensahero.mobile.gateway.presentation.setup

import mensahero.mobile.gateway.data.local.model.SetupStep
import mensahero.mobile.gateway.data.local.model.SetupUserData

data class SetupState(
    val currentStepIndex: Int = 0,
    val steps: List<SetupStep> = SetupStep.getDefaultSteps(),
    val userData: SetupUserData = SetupUserData(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSetupComplete: Boolean = false,
    val canNavigateNext: Boolean = true,
    val canNavigateBack: Boolean = false
) {
    val currentStep: SetupStep?
        get() = steps.getOrNull(currentStepIndex)

    val isLastStep: Boolean
        get() = currentStepIndex == steps.size - 1

    val isFirstStep: Boolean
        get() = currentStepIndex == 0

    val progress: Float
        get() = if (steps.isEmpty()) 0f else (currentStepIndex + 1) / steps.size.toFloat()
}
