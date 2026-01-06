package mensahero.mobile.gateway.presentation.setup.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PlugZap
import com.composables.icons.lucide.Server
import com.isakaro.kwik.ui.button.KwikButton
import mensahero.mobile.gateway.data.local.model.SetupServerData

@Composable
fun ServerSetupStepScreen(
    serverData: SetupServerData,
    onServerChange: (String) -> Unit,
    onWebsocketServerChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = serverData.apiServer,
            onValueChange = onServerChange,
            label = { Text("API Server") },
            placeholder = { Text("API server address", fontSize = 10.sp) },
            leadingIcon = {
                Image(Lucide.Server, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = serverData.websocketServer,
            onValueChange = onWebsocketServerChange,
            label = { Text("Websocket Server") },
            placeholder = { Text("Websocket server address", fontSize = 10.sp) },
            leadingIcon = {
                Image(Lucide.PlugZap, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        // test connection
        KwikButton(
            text= "Test Connection",
            onClick = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Your server address information is stored locally and securely on your device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
