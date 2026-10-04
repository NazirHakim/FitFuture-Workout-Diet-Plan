package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = ElectricFlame,
  onPrimary = Color.White,
  primaryContainer = Color(0xFF3E170C),
  onPrimaryContainer = Color(0xFFFFDBCF),
  secondary = CyberCyan,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF004D5A),
  onSecondaryContainer = Color(0xFFB5F4FF),
  tertiary = VoltLime,
  onTertiary = Color.Black,
  tertiaryContainer = Color(0xFF384300),
  onTertiaryContainer = Color(0xFFE8FFA4),
  background = CarbonDark,
  onBackground = TextHighEmphasis,
  surface = CarbonSurface,
  onSurface = TextHighEmphasis,
  surfaceVariant = CarbonSurfaceVariant,
  onSurfaceVariant = TextMediumEmphasis,
  outline = CarbonBorder,
  error = DangerRed,
  onError = Color.White
)

private val LightColorScheme = lightColorScheme(
  primary = ElectricFlame,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFFDBCF),
  onPrimaryContainer = Color(0xFF3E170C),
  secondary = Color(0xFF00838F),
  onSecondary = Color.White,
  tertiary = Color(0xFF558B2F),
  onTertiary = Color.White,
  background = Color(0xFFF8F9FA),
  onBackground = Color(0xFF191C1E),
  surface = Color.White,
  onSurface = Color(0xFF191C1E),
  surfaceVariant = Color(0xFFE7E0EC),
  onSurfaceVariant = Color(0xFF49454F),
  outline = Color(0xFFC7CBD1),
  error = DangerRed,
  onError = Color.White
)

@Composable
fun IronPulseTheme(
  darkTheme: Boolean = true, // Default to energetic dark theme
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  IronPulseTheme(darkTheme = true, content = content)
}
