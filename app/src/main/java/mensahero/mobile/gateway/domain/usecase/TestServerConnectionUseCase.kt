package mensahero.mobile.gateway.domain.usecase


import mensahero.mobile.gateway.data.remote.dto.ConnectionTestResult
import mensahero.mobile.gateway.data.repository.ServerRepository
import javax.inject.Inject
/**
 * Use case for testing server connection
 */
class TestServerConnectionUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    /**
     * Test connection to API server
     */
    suspend operator fun invoke(serverUrl: String): Result<ConnectionTestResult> {
        // Validate URL format
        if (!isValidUrl(serverUrl)) {
            return Result.failure(
                IllegalArgumentException("Invalid server URL format. Must start with http:// or https://")
            )
        }

        return serverRepository.testServerConnection(serverUrl)
    }

    private fun isValidUrl(url: String): Boolean {
        val trimmed = url.trim()
        return trimmed.startsWith("http://") || trimmed.startsWith("https://")
    }
}