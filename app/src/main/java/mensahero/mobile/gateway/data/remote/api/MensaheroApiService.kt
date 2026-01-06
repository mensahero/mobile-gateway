package mensahero.mobile.gateway.data.remote.api

import mensahero.mobile.gateway.data.remote.dto.ServerHealthResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * API Service for Mensahero Laravel Backend
 * Defines all API endpoints
 */
interface MensaheroApiService {

    /**
     * Test server connection and health
     * Typical Laravel health check endpoint
     */
    @GET
    suspend fun checkServerHealth(@Url url: String): Response<ServerHealthResponse>

    /**
     * Alternative health check if Laravel uses /api/health
     */
    @GET("api/health")
    suspend fun checkHealth(): Response<ServerHealthResponse>

    /**
     * Check API version
     */
    @GET("api/version")
    suspend fun getApiVersion(): Response<Map<String, String>>
}