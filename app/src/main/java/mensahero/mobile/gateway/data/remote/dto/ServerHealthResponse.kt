package mensahero.mobile.gateway.data.remote.dto

import com.google.gson.annotations.SerializedName
/**
 * Server Health Response DTO
 * Matches Laravel health check response format
 */
data class ServerHealthResponse(
    @SerializedName("status")
    val status: String, // "ok", "healthy", "up"

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("timestamp")
    val timestamp: Long? = null,

    @SerializedName("version")
    val version: String? = null,

    @SerializedName("data")
    val data: Map<String, Any>? = null
)
