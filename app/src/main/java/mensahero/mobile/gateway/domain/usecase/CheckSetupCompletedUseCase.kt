package mensahero.mobile.gateway.domain.usecase

import mensahero.mobile.gateway.data.repository.SetupRepository

class CheckSetupCompletedUseCase(
    private val repository: SetupRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isSetupCompleted()
    }
}