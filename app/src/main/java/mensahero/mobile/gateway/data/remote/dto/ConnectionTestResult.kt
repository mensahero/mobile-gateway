package mensahero.mobile.gateway.data.remote.dto


import com.google.gson.annotations.SerializedName
/**
 * Connection Test Result
 */
data class ConnectionTestResult(
    val isSuccessful: Boolean,
    val message: String,
    val responseTime: Long? = null,
    val serverVersion: String? = null
)
