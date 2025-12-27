package mensahero.mobile.gateway

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mensahero.mobile.gateway.presentation.main.MainViewModel
import mensahero.mobile.gateway.presentation.setup.SetupActivity
import mensahero.mobile.gateway.ui.theme.MensaheroMobileGatewayTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MensaheroMobileGatewayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        onNavigateToSetup = ::navigateToSetup
                    )
                }
            }
        }
    }

    private fun navigateToSetup() {
        val intent = Intent(this, SetupActivity::class.java)
        startActivity(intent)
        finish() // Prevent user from going back to MainActivity before setup is done
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToSetup: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Handle navigation based on setup status
    LaunchedEffect(state.setupCompleted) {
        if (state.setupCompleted == false) {
            onNavigateToSetup()
        }
    }

    when {
        state.isLoading -> {
            LoadingScreen()
        }
        state.setupCompleted == true -> {
            MainContent()
        }
        state.error != null -> {
            ErrorScreen(
                error = state.error ?: "Unknown error",
                onRetry = { viewModel.retryCheckSetup() }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MainContent() {
    // Your main app content here
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Setup Done. Welcome to Mensahero!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun ErrorScreen(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error
            )
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}