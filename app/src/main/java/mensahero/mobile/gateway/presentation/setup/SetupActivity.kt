package mensahero.mobile.gateway.presentation.setup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.isakaro.kwik.ui.button.KwikButton
import com.isakaro.kwik.ui.stepper.*
import dagger.hilt.android.AndroidEntryPoint
import mensahero.mobile.gateway.MainActivity
import mensahero.mobile.gateway.data.local.model.SetupStep
import mensahero.mobile.gateway.presentation.setup.steps.*
import mensahero.mobile.gateway.ui.theme.MensaheroMobileGatewayTheme

/**
 * SetupActivity - Handles the initial setup flow with Hilt dependency injection
 * Uses KwikStepper for step navigation
 */
@AndroidEntryPoint
class SetupActivity : ComponentActivity() {

    private val viewModel: SetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MensaheroMobileGatewayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupScreen(
                        viewModel = viewModel,
                        onSetupComplete = ::navigateToMain
                    )
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    viewModel: SetupViewModel,
    onSetupComplete: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Create KwikStepper state
    val kwikStepperState = rememberKwikStepperState(
        steps = state.steps.map { it.title }
    )


    // Track previous step index to detect changes from ViewModel
    var previousStepIndex by remember { mutableStateOf(state.currentStepIndex) }

    // Sync KwikStepper with ViewModel state
    // Only update stepper when ViewModel actually changes the step (validation passed)
    LaunchedEffect(state.currentStepIndex) {
        if (state.currentStepIndex != previousStepIndex) {
            // ViewModel changed the step, so update the stepper
            if (state.currentStepIndex > previousStepIndex) {
                kwikStepperState.moveForward()
            } else {
                kwikStepperState.moveBackward()
            }
            previousStepIndex = state.currentStepIndex
        }
    }

    // Handle setup completion
    LaunchedEffect(state.isSetupComplete) {
        if (state.isSetupComplete) {
            onSetupComplete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Setup Mensahero Mobile Gateway",
                        style = MaterialTheme.typography.titleSmall,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // KwikStepper component
            KwikStepper(
                state = kwikStepperState,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (state.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.onEvent(SetupEvent.ClearError) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Step content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (state.currentStepIndex) {
                    SetupStep.STEP_WELCOME -> WelcomeStepScreen()
                    SetupStep.STEP_SERVER -> ServerSetupStepScreen(
                        serverData = state.serverData,
                        onServerChange = { viewModel.onEvent(SetupEvent.UpdateApiServer(it)) },
                        onWebsocketServerChange = { viewModel.onEvent(SetupEvent.UpdateWebsocketServer(it)) },
                    )

                    SetupStep.STEP_PROFILE -> ProfileSetupStepScreen(
                        userData = state.userData,
                        onNameChange = { viewModel.onEvent(SetupEvent.UpdateUserName(it)) },
                        onEmailChange = { viewModel.onEvent(SetupEvent.UpdateUserEmail(it)) }
                    )

                    SetupStep.STEP_PREFERENCES -> PreferencesStepScreen(
                        userData = state.userData,
                        onNotificationChange = {
                            viewModel.onEvent(SetupEvent.UpdateNotificationEnabled(it))
                        },
                        onDarkModeChange = {
                            viewModel.onEvent(SetupEvent.UpdateDarkModeEnabled(it))
                        }
                    )

                    SetupStep.STEP_COMPLETE -> CompleteStepScreen()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KwikButton(
                    text = "Previous",
                    isLoading = state.isLoading,
                    enabled = state.canNavigateBack && !state.isLoading,
                    outlined = true,
                    onClick = {
                        viewModel.onEvent(SetupEvent.PreviousStep)
                    },
                )

                KwikButton(
                    text = if (state.isLastStep) "Finish" else "Next",
                    enabled = !state.isLoading,
                    isLoading = state.isLoading,
                    trailingIcon = if (state.isLastStep) Lucide.Check else Lucide.ArrowRight,
                    onClick = {
                        if (state.isLastStep) {
                            // Complete setup
                            viewModel.onEvent(SetupEvent.CompleteSetup)
                            // Stepper completion handled separately
                            if (!state.isLoading && state.error == null) {
                                kwikStepperState.completeAll()
                            }
                        } else {
                            viewModel.onEvent(SetupEvent.NextStep)
                        }
                    },
                )
            }
        }
    }
}