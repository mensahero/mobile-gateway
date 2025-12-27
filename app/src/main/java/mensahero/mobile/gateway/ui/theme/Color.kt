package mensahero.mobile.gateway.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Mensahero Brand Colors
 * Official brand identity colors for the SMS Gateway and Messaging Platform
 *
 * Brand Identity:
 * - Deep Blue: #1E4DD8 (Primary brand color)
 * - Sky Blue: #4DB8FF (Secondary/Accent)
 * - Charcoal: #1B1B1B (Dark neutral)
 * - Off-White: #F8FAFC (Light neutral)
 */

// ==================== PRIMARY BRAND COLORS ====================

/** Mensahero Deep Blue - Primary brand color */
val MensaheroDeepBlue = Color(0xFF1E4DD8)

/** Mensahero Sky Blue - Secondary/Accent color */
val MensaheroSkyBlue = Color(0xFF4DB8FF)

/** Mensahero Charcoal - Dark neutral */
val MensaheroCharcoal = Color(0xFF1B1B1B)

/** Mensahero Off-White - Light neutral */
val MensaheroOffWhite = Color(0xFFF8FAFC)

// ==================== LIGHT THEME COLORS ====================

// Primary - Deep Blue variations
val MensaheroDeepBlueDark = Color(0xFF1539A1)        // Darker for pressed states
val MensaheroDeepBlueLight = Color(0xFF5B7FE8)       // Lighter for containers
val MensaheroDeepBlueLighter = Color(0xFFE3EBFD)     // Very light for backgrounds

// Secondary - Sky Blue variations
val MensaheroSkyBlueDark = Color(0xFF2A8FCC)         // Darker sky blue
val MensaheroSkyBlueLight = Color(0xFF8DD4FF)        // Lighter sky blue
val MensaheroSkyBlueLighter = Color(0xFFE5F5FF)      // Very light for backgrounds

// Neutral colors for light theme
val MensaheroGray50 = Color(0xFFFAFAFA)              // Lightest gray
val MensaheroGray100 = Color(0xFFF5F5F5)             // Very light gray
val MensaheroGray200 = Color(0xFFEEEEEE)             // Light gray
val MensaheroGray300 = Color(0xFFE0E0E0)             // Medium-light gray
val MensaheroGray400 = Color(0xFFBDBDBD)             // Medium gray
val MensaheroGray500 = Color(0xFF9E9E9E)             // Gray
val MensaheroGray600 = Color(0xFF757575)             // Medium-dark gray
val MensaheroGray700 = Color(0xFF616161)             // Dark gray
val MensaheroGray800 = Color(0xFF424242)             // Very dark gray
val MensaheroGray900 = Color(0xFF212121)             // Almost black

// Text colors for light theme
val MensaheroTextPrimary = Color(0xFF1B1B1B)         // Almost black (matches Charcoal)
val MensaheroTextSecondary = Color(0xFF616161)       // Dark gray
val MensaheroTextTertiary = Color(0xFF9E9E9E)        // Medium gray
val MensaheroTextOnPrimary = Color(0xFFFFFFFF)       // White on primary colors

// ==================== DARK THEME COLORS ====================

// Primary - Brighter blues for dark mode
val MensaheroDeepBlueDarkMode = Color(0xFF5B7FE8)    // Brighter deep blue
val MensaheroDeepBlueLightDark = Color(0xFF8BA3F0)   // Even lighter for containers
val MensaheroDeepBlueDarkerDark = Color(0xFF1E4DD8)  // Original for emphasis

// Secondary - Brighter sky blue for dark mode
val MensaheroSkyBlueDarkMode = Color(0xFF8DD4FF)     // Brighter sky blue
val MensaheroSkyBlueLightDark = Color(0xFFB8E5FF)    // Very light for containers
val MensaheroSkyBlueDarkerDark = Color(0xFF4DB8FF)   // Original for emphasis

// Surface colors for dark theme
val MensaheroSurfaceDark = Color(0xFF1E1E1E)         // Slightly lighter than pure black
val MensaheroBackgroundDark = Color(0xFF121212)      // True dark background
val MensaheroSurfaceVariantDark = Color(0xFF2C2C2C)  // Elevated surface

// Text colors for dark theme
val MensaheroTextPrimaryDark = Color(0xFFF8FAFC)     // Off-white (matches brand)
val MensaheroTextSecondaryDark = Color(0xFFBDBDBD)   // Light gray
val MensaheroTextTertiaryDark = Color(0xFF9E9E9E)    // Medium gray
val MensaheroTextOnPrimaryDark = Color(0xFF000000)   // Black on bright colors

// ==================== STATUS & SEMANTIC COLORS ====================

// Success colors
val MensaheroSuccess = Color(0xFF10B981)             // Green success
val MensaheroSuccessLight = Color(0xFFD1FAE5)        // Light green background
val MensaheroSuccessDark = Color(0xFF059669)         // Dark green
val MensaheroSuccessDarkMode = Color(0xFF34D399)     // Bright green for dark mode

// Error colors
val MensaheroError = Color(0xFFEF4444)               // Red error
val MensaheroErrorLight = Color(0xFFFEE2E2)          // Light red background
val MensaheroErrorDark = Color(0xFFDC2626)           // Dark red
val MensaheroErrorDarkMode = Color(0xFFF87171)       // Bright red for dark mode

// Warning colors
val MensaheroWarning = Color(0xFFF59E0B)             // Orange warning
val MensaheroWarningLight = Color(0xFFFEF3C7)        // Light orange background
val MensaheroWarningDark = Color(0xFFD97706)         // Dark orange
val MensaheroWarningDarkMode = Color(0xFFFBBF24)     // Bright orange for dark mode

// Info colors (using Sky Blue)
val MensaheroInfo = MensaheroSkyBlue                 // Sky blue info
val MensaheroInfoLight = Color(0xFFE0F2FE)           // Light blue background
val MensaheroInfoDark = Color(0xFF0284C7)            // Dark blue
val MensaheroInfoDarkMode = MensaheroSkyBlueDarkMode // Bright blue for dark mode

// ==================== ADDITIONAL UTILITY COLORS ====================

/** Outline colors for borders and dividers */
val MensaheroOutlineLight = Color(0xFFE0E0E0)        // Light border
val MensaheroOutlineDark = Color(0xFF424242)         // Dark border

/** Divider colors */
val MensaheroDividerLight = Color(0x1F000000)        // 12% black
val MensaheroDividerDark = Color(0x1FFFFFFF)         // 12% white

/** Scrim for dialogs and bottom sheets */
val MensaheroScrim = Color(0x80000000)               // 50% black

/** Focus/Selection colors */
val MensaheroFocusLight = MensaheroDeepBlue.copy(alpha = 0.12f)
val MensaheroFocusDark = MensaheroDeepBlueDarkMode.copy(alpha = 0.24f)

// ==================== LEGACY COLORS (can be removed) ====================

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroDeepBlue"))
val Purple80 = Color(0xFFD0BCFF)

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroGray400"))
val PurpleGrey80 = Color(0xFFCCC2DC)

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroSkyBlue"))
val Pink80 = Color(0xFFEFB8C8)

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroDeepBlue"))
val Purple40 = Color(0xFF6650a4)

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroGray600"))
val PurpleGrey40 = Color(0xFF625b71)

@Deprecated("Use Mensahero brand colors instead", ReplaceWith("MensaheroSkyBlueDark"))
val Pink40 = Color(0xFF7D5260)