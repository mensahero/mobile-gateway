package mensahero.mobile.gateway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mensahero.mobile.gateway.ui.theme.MensaheroMobileGatewayTheme
import mensahero.mobile.gateway.utils.Preferences

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val preferences by lazy { getSharedPreferences("app_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check setup status
        val isSetupDone = preferences.getBoolean(Preferences.KEY_INITIAL_SETUP_DONE, false)

        setContent {
            MensaheroMobileGatewayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(text = "Hello World!", modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}