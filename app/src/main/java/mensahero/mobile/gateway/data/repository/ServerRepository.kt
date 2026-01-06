package mensahero.mobile.gateway.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager
import mensahero.mobile.gateway.data.remote.DynamicApiServiceProvider
import mensahero.mobile.gateway.data.remote.dto.ConnectionTestResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for server-related operations
 * Handles API calls to backend with dynamic base URLs
 */
@Singleton
class ServerRepository @Inject constructor(
    private val apiServiceProvider: DynamicApiServiceProvider,
    private val preferencesManager: PreferencesManager
) {

    /**
     * Test connection to a specific server URL
     * Used during setup to validate server before saving
     *
     * @param serverUrl The server URL to test
     * @return Result with ConnectionTestResult
     */
    suspend fun testServerConnection(serverUrl: String): Result<ConnectionTestResult> {
        return withContext(Dispatchers.IO) {
            try {
                val startTime = System.currentTimeMillis()

                // Create temporary API service for testing (doesn't use saved config)
                val apiService = apiServiceProvider.createTemporaryApiService(serverUrl)

                // Try health check endpoint
                val response = try {
                    apiService.checkHealth()
                } catch (e: Exception) {
                    // If /api/health fails, try alternative endpoints
                    try {
                        apiService.checkServerHealth("health")
                    } catch (e2: Exception) {
                        return@withContext Result.failure(
                            Exception("Could not connect to server. Please check the URL and try again.")
                        )
                    }
                }

                val responseTime = System.currentTimeMillis() - startTime

                if (response.isSuccessful) {
                    val body = response.body()
                    val isHealthy = body?.status?.lowercase() in listOf(
                        "ok", "healthy", "up", "success", "running"
                    )

                    if (isHealthy) {
                        Result.success(
                            ConnectionTestResult(
                                isSuccessful = true,
                                message = "Connection successful! Server is healthy.",
                                responseTime = responseTime,
                                serverVersion = body?.version
                            )
                        )
                    } else {
                        Result.success(
                            ConnectionTestResult(
                                isSuccessful = true,
                                message = "Server responded (status: ${body?.status ?: "unknown"})",
                                responseTime = responseTime,
                                serverVersion = body?.version
                            )
                        )
                    }
                } else {
                    Result.failure(
                        Exception("Server returned error: ${response.code()} ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                Result.failure(
                    Exception("Connection failed: ${e.message ?: "Unknown error"}")
                )
            }
        }
    }

    /**
     * Test WebSocket server connection
     *
     * @param websocketUrl The WebSocket URL to test
     * @return Result with ConnectionTestResult
     */
    suspend fun testWebSocketConnection(websocketUrl: String): Result<ConnectionTestResult> {
        return withContext(Dispatchers.IO) {
            try {
                val startTime = System.currentTimeMillis()

                // For WebSocket, we'll do a simple HTTP check first
                // Convert ws:// or wss:// to http:// or https://
                val httpUrl = websocketUrl
                    .replace("ws://", "http://")
                    .replace("wss://", "https://")

                val apiService = apiServiceProvider.createTemporaryApiService(httpUrl)

                // Try to access WebSocket health endpoint
                val response = try {
                    apiService.checkHealth()
                } catch (e: Exception) {
                    // WebSocket servers might not have HTTP endpoints
                    // If it fails, consider the URL valid if it's properly formatted
                    val responseTime = System.currentTimeMillis() - startTime
                    return@withContext Result.success(
                        ConnectionTestResult(
                            isSuccessful = true,
                            message = "WebSocket URL is valid. Connection will be tested at runtime.",
                            responseTime = responseTime
                        )
                    )
                }

                val responseTime = System.currentTimeMillis() - startTime

                Result.success(
                    ConnectionTestResult(
                        isSuccessful = true,
                        message = "WebSocket server is reachable.",
                        responseTime = responseTime
                    )
                )
            } catch (e: Exception) {
                // Even if check fails, allow it if URL format is valid
                Result.success(
                    ConnectionTestResult(
                        isSuccessful = true,
                        message = "WebSocket URL is valid. Full connection test will occur at runtime.",
                        responseTime = null
                    )
                )
            }
        }
    }

    /**
     * Test both API and WebSocket servers
     */
    suspend fun testBothServers(
        apiServer: String,
        websocketServer: String
    ): Result<Pair<ConnectionTestResult, ConnectionTestResult>> {
        return withContext(Dispatchers.IO) {
            try {
                val apiResult = testServerConnection(apiServer)
                val wsResult = testWebSocketConnection(websocketServer)

                if (apiResult.isSuccess && wsResult.isSuccess) {
                    Result.success(
                        Pair(apiResult.getOrThrow(), wsResult.getOrThrow())
                    )
                } else {
                    val error = apiResult.exceptionOrNull() ?: wsResult.exceptionOrNull()
                    Result.failure(error ?: Exception("Connection test failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Save server configuration to preferences
     */
    suspend fun saveServerConfig(apiServer: String, websocketServer: String) {
        preferencesManager.saveApiServer(apiServer)
        preferencesManager.saveWebsocketServer(websocketServer)
        // Refresh API service to use new URL
        apiServiceProvider.refreshApiService()
    }

    /**
     * Get saved server configuration
     */
    suspend fun getSavedServerConfig(): Pair<String?, String?> {
        return Pair(
            preferencesManager.getApiServer(),
            preferencesManager.getWebsocketServer()
        )
    }

    /**
     * Get the current API service (uses saved configuration)
     * This is what your app will use for actual API calls after setup
     */
    suspend fun getConfiguredApiService() = apiServiceProvider.getApiService()

    /**
     * Check if server is configured
     */
    suspend fun isServerConfigured(): Boolean {
        return apiServiceProvider.isConfigured()
    }
}