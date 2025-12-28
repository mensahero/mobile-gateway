package mensahero.mobile.gateway.domain.usecase

import mensahero.mobile.gateway.data.local.model.SetupServerData
import mensahero.mobile.gateway.data.local.model.SetupUserData
import mensahero.mobile.gateway.data.repository.SetupRepository

class CompleteSetupUseCase(
    private val repository: SetupRepository
) {
    suspend operator fun invoke(userData: SetupUserData, serverData: SetupServerData): Result<Unit> {
        if (!userData.isProfileValid()) {
            return Result.failure(IllegalArgumentException("Invalid user data"))
        }
        return repository.completeSetup(userData, serverData)
    }
}