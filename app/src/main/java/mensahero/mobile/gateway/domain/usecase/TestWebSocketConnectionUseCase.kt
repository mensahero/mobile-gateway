package mensahero.mobile.gateway.domain.usecase


import mensahero.mobile.gateway.data.remote.dto.ConnectionTestResult
import mensahero.mobile.gateway.data.repository.ServerRepository
import javax.inject.Inject

/**
 * Use case for testing WebSocket server connection
 */
class TestWebSocketConnectionUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    /**
     * Test connection to WebSocket server
     */
    suspend operator fun invoke(websocketUrl: String): Result<ConnectionTestResult> {
        // Validate URL format
        if (!isValidUrl(websocketUrl)) {
            return Result.failure(
                IllegalArgumentException("Invalid WebSocket URL format. Must start with ws:// or wss:// or http:// or https://")
            )
        }

        return serverRepository.testWebSocketConnection(websocketUrl)
    }

    private fun isValidUrl(url: String): Boolean {
        val trimmed = url.trim()
        return trimmed.startsWith("http://") ||
                trimmed.startsWith("https://") ||
                trimmed.startsWith("ws://") ||
                trimmed.startsWith("wss://")
    }
}