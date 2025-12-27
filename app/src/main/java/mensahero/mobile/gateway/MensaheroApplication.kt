package mensahero.mobile.gateway

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp
 * This triggers Hilt's code generation and sets up the application-level dependency container
 */
@HiltAndroidApp
class MensaheroApplication : Application()