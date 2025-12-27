package mensahero.mobile.gateway.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mensahero.mobile.gateway.domain.usecase.CompleteSetupUseCase

class SetupViewModelFactory(
    private val completeSetupUseCase: CompleteSetupUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetupViewModel::class.java)) {
            return SetupViewModel(completeSetupUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}