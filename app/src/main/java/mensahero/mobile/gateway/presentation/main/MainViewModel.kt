package mensahero.mobile.gateway.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mensahero.mobile.gateway.domain.usecase.CheckSetupCompletedUseCase
import javax.inject.Inject

/**
 * State for MainActivity
 */
data class MainState(
    val isLoading: Boolean = true,
    val setupCompleted: Boolean? = null,
    val error: String? = null
)

/**
 * ViewModel for MainActivity
 * Checks setup status on initialization using Hilt dependency injection
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkSetupCompletedUseCase: CheckSetupCompletedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        checkSetupStatus()
    }

    private fun checkSetupStatus() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val isCompleted = checkSetupCompletedUseCase()
                _state.update {
                    it.copy(
                        isLoading = false,
                        setupCompleted = isCompleted,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        setupCompleted = null,
                        error = e.message ?: "Failed to check setup status"
                    )
                }
            }
        }
    }

    fun retryCheckSetup() {
        checkSetupStatus()
    }
}