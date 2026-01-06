package mensahero.mobile.gateway.data.remote

import com.google.gson.Gson
import mensahero.mobile.gateway.data.local.preferences.PreferencesManager
import mensahero.mobile.gateway.data.remote.api.MensaheroApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Dynamic API Service Provider
 * Creates Retrofit instances with base URL from saved preferences
 * This allows the API client to use the server configured during setup
 */
@Singleton
class DynamicApiServiceProvider @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {

    private var cachedApiService: MensaheroApiService? = null
    private var cachedBaseUrl: String? = null

    /**
     * Get API service with dynamic base URL from preferences
     * Creates a new instance if base URL has changed
     */
    suspend fun getApiService(): MensaheroApiService {
        val currentBaseUrl = preferencesManager.getApiServer()

        // Return cached service if URL hasn't changed
        if (cachedApiService != null && cachedBaseUrl == currentBaseUrl) {
            return cachedApiService!!
        }

        // Create new service with current base URL
        val baseUrl = currentBaseUrl?.let { cleanAndValidateUrl(it) }
            ?: getDefaultBaseUrl()

        cachedBaseUrl = baseUrl
        cachedApiService = createApiService(baseUrl)

        return cachedApiService!!
    }

    /**
     * Force refresh the API service (useful after server configuration changes)
     */
    suspend fun refreshApiService(): MensaheroApiService {
        cachedApiService = null
        cachedBaseUrl = null
        return getApiService()
    }

    /**
     * Get WebSocket server URL from preferences
     */
    suspend fun getWebSocketUrl(): String {
        return preferencesManager.getWebsocketServer()?.let { cleanAndValidateUrl(it) }
            ?: getDefaultWebSocketUrl()
    }

    /**
     * Create Retrofit API service with specific base URL
     */
    private fun createApiService(baseUrl: String): MensaheroApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(MensaheroApiService::class.java)
    }

    /**
     * Create temporary API service for testing (used during setup)
     * This doesn't use cached values
     */
    fun createTemporaryApiService(baseUrl: String): MensaheroApiService {
        val cleanUrl = cleanAndValidateUrl(baseUrl)
        return createApiService(cleanUrl)
    }

    /**
     * Clean and validate URL
     * Ensures URL has protocol and proper formatting
     */
    private fun cleanAndValidateUrl(url: String): String {
        var clean = url.trim()

        // Add https:// if no protocol specified
        if (!clean.startsWith("http://") && !clean.startsWith("https://")) {
            clean = "https://$clean"
        }

        // Remove trailing slash
        clean = clean.trimEnd('/')

        // Add trailing slash for Retrofit (base URL requirement)
        return "$clean/"
    }

    /**
     * Default base URL (fallback if no configuration exists)
     */
    private fun getDefaultBaseUrl(): String {
        return "https://api.mensahero.com/"
    }

    /**
     * Default WebSocket URL (fallback)
     */
    private fun getDefaultWebSocketUrl(): String {
        return "wss://ws.mensahero.com/"
    }

    /**
     * Check if API service is configured
     */
    suspend fun isConfigured(): Boolean {
        val apiServer = preferencesManager.getApiServer()
        return !apiServer.isNullOrBlank()
    }
}