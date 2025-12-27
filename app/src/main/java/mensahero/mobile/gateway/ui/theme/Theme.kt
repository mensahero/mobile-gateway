package mensahero.mobile.gateway.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Mensahero Dark Color Scheme
 *
 * "Reliable. Fast. Local." — A modern SMS Gateway theme
 * Built with Mensahero's brand identity: Deep Blue, Sky Blue, Charcoal, Off-White
 */
private val MensaheroDarkColorScheme = darkColorScheme(
    // Primary - Deep Blue (brighter for dark mode)
    primary = MensaheroDeepBlueDarkMode,
    onPrimary = MensaheroTextOnPrimaryDark,
    primaryContainer = MensaheroDeepBlueLightDark,
    onPrimaryContainer = Color(0xFFE3EBFD),

    // Secondary - Sky Blue
    secondary = MensaheroSkyBlueDarkMode,
    onSecondary = MensaheroTextOnPrimaryDark,
    secondaryContainer = MensaheroSkyBlueLightDark,
    onSecondaryContainer = Color(0xFFE5F5FF),

    // Tertiary - Lighter variation of Deep Blue
    tertiary = MensaheroDeepBlueLightDark,
    onTertiary = MensaheroTextOnPrimaryDark,
    tertiaryContainer = Color(0xFF2C3E6B),
    onTertiaryContainer = Color(0xFFD6E3FF),

    // Error
    error = MensaheroErrorDarkMode,
    onError = Color(0xFF000000),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Background & Surface
    background = MensaheroBackgroundDark,
    onBackground = MensaheroTextPrimaryDark,
    surface = MensaheroSurfaceDark,
    onSurface = MensaheroTextPrimaryDark,
    surfaceVariant = MensaheroSurfaceVariantDark,
    onSurfaceVariant = MensaheroTextSecondaryDark,

    // Outline & extras
    outline = MensaheroOutlineDark,
    outlineVariant = Color(0xFF3A3A3A),
    scrim = MensaheroScrim,
    inverseSurface = MensaheroOffWhite,
    inverseOnSurface = MensaheroCharcoal,
    inversePrimary = MensaheroDeepBlue,
    surfaceTint = MensaheroDeepBlueDarkMode,
)

/**
 * Mensahero Light Color Scheme
 *
 * Clean, professional design using Mensahero's brand colors
 */
private val MensaheroLightColorScheme = lightColorScheme(
    // Primary - Deep Blue
    primary = MensaheroDeepBlue,
    onPrimary = MensaheroTextOnPrimary,
    primaryContainer = MensaheroDeepBlueLighter,
    onPrimaryContainer = MensaheroDeepBlueDark,

    // Secondary - Sky Blue
    secondary = MensaheroSkyBlue,
    onSecondary = MensaheroTextOnPrimary,
    secondaryContainer = MensaheroSkyBlueLighter,
    onSecondaryContainer = MensaheroSkyBlueDark,

    // Tertiary - Darker variation of Sky Blue
    tertiary = Color(0xFF0891B2),
    onTertiary = MensaheroTextOnPrimary,
    tertiaryContainer = Color(0xFFCFFAFE),
    onTertiaryContainer = Color(0xFF164E63),

    // Error
    error = MensaheroError,
    onError = Color(0xFFFFFFFF),
    errorContainer = MensaheroErrorLight,
    onErrorContainer = MensaheroErrorDark,

    // Background & Surface
    background = MensaheroOffWhite,
    onBackground = MensaheroTextPrimary,
    surface = Color(0xFFFFFFFF),
    onSurface = MensaheroTextPrimary,
    surfaceVariant = MensaheroGray100,
    onSurfaceVariant = MensaheroTextSecondary,

    // Outline & extras
    outline = MensaheroOutlineLight,
    outlineVariant = MensaheroGray200,
    scrim = MensaheroScrim,
    inverseSurface = MensaheroCharcoal,
    inverseOnSurface = MensaheroOffWhite,
    inversePrimary = MensaheroDeepBlueDarkMode,
    surfaceTint = MensaheroDeepBlue,
)

/**
 * Mensahero Mobile Gateway Theme
 *
 * Official theme for Mensahero - the modern SMS Gateway and Messaging Platform
 *
 * Brand Values:
 * - Reliable: Trustworthy Deep Blue as primary
 * - Fast: Dynamic Sky Blue for energy
 * - Local: Filipino innovation with pride 🇵🇭
 *
 * @param darkTheme Whether to use dark theme (defaults to system setting)
 * @param dynamicColor Whether to use Material You dynamic colors on Android 12+ (defaults to false to maintain brand)
 * @param content The composable content to theme
 */
@Composable
fun MensaheroMobileGatewayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled by default to maintain Mensahero branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Only use dynamic color if explicitly enabled AND Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        // Use Mensahero brand colors (default)
        darkTheme -> MensaheroDarkColorScheme
        else -> MensaheroLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Preview variations for different themes
 */
@Composable
fun MensaheroMobileGatewayThemeLight(
    content: @Composable () -> Unit
) {
    MensaheroMobileGatewayTheme(
        darkTheme = false,
        dynamicColor = false,
        content = content
    )
}

@Composable
fun MensaheroMobileGatewayThemeDark(
    content: @Composable () -> Unit
) {
    MensaheroMobileGatewayTheme(
        darkTheme = true,
        dynamicColor = false,
        content = content
    )
}