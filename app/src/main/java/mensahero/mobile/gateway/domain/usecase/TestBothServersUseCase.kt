package mensahero.mobile.gateway.domain.usecase


import mensahero.mobile.gateway.data.remote.dto.ConnectionTestResult
import mensahero.mobile.gateway.data.repository.ServerRepository
import javax.inject.Inject
/**
 * Use case for testing both servers together
 */
class TestBothServersUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {
    suspend operator fun invoke(
        apiServer: String,
        websocketServer: String
    ): Result<Pair<ConnectionTestResult, ConnectionTestResult>> {
        return serverRepository.testBothServers(apiServer, websocketServer)
    }
}