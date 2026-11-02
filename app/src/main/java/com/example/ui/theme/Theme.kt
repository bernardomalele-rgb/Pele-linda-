package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Pele Linda signature palette: modern, soft, feminine (rose, cream, mint, honey)
// High-contrast dark text ensures all letters, numbers, and inputs are always crystal clear.
private val PeleLindaColorScheme = lightColorScheme(
  primary = RosePrimary,
  onPrimary = RoseOnPrimary,
  primaryContainer = RoseContainer,
  onPrimaryContainer = RoseOnContainer,
  secondary = MintSecondary,
  onSecondary = MintOnSecondary,
  secondaryContainer = MintContainer,
  onSecondaryContainer = MintOnContainer,
  tertiary = HoneyTertiary,
  onTertiary = HoneyOnContainer,
  tertiaryContainer = HoneyContainer,
  onTertiaryContainer = HoneyOnContainer,
  background = CreamBackground,
  onBackground = TextDark,
  surface = CreamSurface,
  onSurface = TextDark,
  surfaceVariant = CreamSurfaceVariant,
  onSurfaceVariant = TextMuted,
  outline = OutlineSoft,
)

@Composable
fun PeleLindaTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = PeleLindaColorScheme,
    typography = Typography,
    content = content
  )
}

// Alias for backwards compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  PeleLindaTheme(darkTheme = darkTheme, content = content)
}

