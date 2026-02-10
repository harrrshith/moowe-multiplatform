package com.harrrshith.moowe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Custom app theme that wraps MaterialTheme and provides a custom color scheme.
 * This allows for more flexibility and dynamic color updates.
 *
 * @param useDarkTheme Whether to use dark theme colors
 * @param typography The typography to use (defaults to AppTypography)
 * @param content The composable content
 */
@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    typography: Typography = appTypography(),
    content: @Composable () -> Unit
) {
    // Provide dark theme state through composition local
    CompositionLocalProvider(LocalIsDarkTheme provides useDarkTheme) {
        // Material theme wrapper for compatibility with Material3 components
        MaterialTheme(
            colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
            typography = typography,
        ) {
            // Determine source color values based on theme
            val sourceColorValues = if (useDarkTheme) {
                darkAppColorScheme()
            } else {
                lightAppColorScheme()
            }

            // Create a stable color scheme instance
            val colorScheme = remember(useDarkTheme) {
                AppColorScheme(sourceColorValues)
            }

            // Update color scheme when source values change
            SideEffect {
                colorScheme.updateFrom(sourceColorValues)
            }

            // Provide custom color scheme through composition local
            CompositionLocalProvider(
                LocalAppColorScheme provides colorScheme,
            ) {
                content()
            }
        }
    }
}

/**
 * Object to access the custom theme properties.
 * Use AppTheme.colorScheme instead of MaterialTheme.colorScheme throughout the app.
 */
object AppTheme {
    /**
     * Returns the current custom color scheme.
     */
    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current

    /**
     * Returns whether the current theme is dark.
     */
    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkTheme.current

    /**
     * Returns the current typography.
     */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

/**
 * CompositionLocal for tracking dark theme state.
 */
internal val LocalIsDarkTheme = staticCompositionLocalOf { false }
