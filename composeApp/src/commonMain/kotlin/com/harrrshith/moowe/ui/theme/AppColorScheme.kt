package com.harrrshith.moowe.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Immutable data class that holds all color values for the app theme.
 * This allows for easy color scheme transitions and animations.
 */
@Stable
data class AppColorValues(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceContainer: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceDim: Color,
    val surfaceBright: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val background: Color,
    val onBackground: Color,
) {

    /**
     * Linearly interpolate between two color schemes for smooth transitions.
     */
    fun lerp(to: AppColorValues, fraction: Float): AppColorValues {
        return AppColorValues(
            primary = lerp(primary, to.primary, fraction),
            onPrimary = lerp(onPrimary, to.onPrimary, fraction),
            primaryContainer = lerp(primaryContainer, to.primaryContainer, fraction),
            onPrimaryContainer = lerp(onPrimaryContainer, to.onPrimaryContainer, fraction),
            secondary = lerp(secondary, to.secondary, fraction),
            onSecondary = lerp(onSecondary, to.onSecondary, fraction),
            secondaryContainer = lerp(secondaryContainer, to.secondaryContainer, fraction),
            onSecondaryContainer = lerp(onSecondaryContainer, to.onSecondaryContainer, fraction),
            tertiary = lerp(tertiary, to.tertiary, fraction),
            onTertiary = lerp(onTertiary, to.onTertiary, fraction),
            tertiaryContainer = lerp(tertiaryContainer, to.tertiaryContainer, fraction),
            onTertiaryContainer = lerp(onTertiaryContainer, to.onTertiaryContainer, fraction),
            surface = lerp(surface, to.surface, fraction),
            onSurface = lerp(onSurface, to.onSurface, fraction),
            surfaceVariant = lerp(surfaceVariant, to.surfaceVariant, fraction),
            onSurfaceVariant = lerp(onSurfaceVariant, to.onSurfaceVariant, fraction),
            surfaceContainer = lerp(surfaceContainer, to.surfaceContainer, fraction),
            surfaceContainerLow = lerp(surfaceContainerLow, to.surfaceContainerLow, fraction),
            surfaceContainerLowest = lerp(surfaceContainerLowest, to.surfaceContainerLowest, fraction),
            surfaceContainerHigh = lerp(surfaceContainerHigh, to.surfaceContainerHigh, fraction),
            surfaceContainerHighest = lerp(surfaceContainerHighest, to.surfaceContainerHighest, fraction),
            surfaceDim = lerp(surfaceDim, to.surfaceDim, fraction),
            surfaceBright = lerp(surfaceBright, to.surfaceBright, fraction),
            inverseSurface = lerp(inverseSurface, to.inverseSurface, fraction),
            inverseOnSurface = lerp(inverseOnSurface, to.inverseOnSurface, fraction),
            inversePrimary = lerp(inversePrimary, to.inversePrimary, fraction),
            outline = lerp(outline, to.outline, fraction),
            outlineVariant = lerp(outlineVariant, to.outlineVariant, fraction),
            scrim = lerp(scrim, to.scrim, fraction),
            error = lerp(error, to.error, fraction),
            onError = lerp(onError, to.onError, fraction),
            errorContainer = lerp(errorContainer, to.errorContainer, fraction),
            onErrorContainer = lerp(onErrorContainer, to.onErrorContainer, fraction),
            background = lerp(background, to.background, fraction),
            onBackground = lerp(onBackground, to.onBackground, fraction),
        )
    }
}

/**
 * Mutable color scheme class that can be updated dynamically.
 * This is the main color scheme class used throughout the app.
 */
@Stable
class AppColorScheme(values: AppColorValues) {

    var primary by mutableStateOf(values.primary)
        internal set

    var onPrimary by mutableStateOf(values.onPrimary)
        internal set

    var primaryContainer by mutableStateOf(values.primaryContainer)
        internal set

    var onPrimaryContainer by mutableStateOf(values.onPrimaryContainer)
        internal set

    var secondary by mutableStateOf(values.secondary)
        internal set

    var onSecondary by mutableStateOf(values.onSecondary)
        internal set

    var secondaryContainer by mutableStateOf(values.secondaryContainer)
        internal set

    var onSecondaryContainer by mutableStateOf(values.onSecondaryContainer)
        internal set

    var tertiary by mutableStateOf(values.tertiary)
        internal set

    var onTertiary by mutableStateOf(values.onTertiary)
        internal set

    var tertiaryContainer by mutableStateOf(values.tertiaryContainer)
        internal set

    var onTertiaryContainer by mutableStateOf(values.onTertiaryContainer)
        internal set

    var surface by mutableStateOf(values.surface)
        internal set

    var onSurface by mutableStateOf(values.onSurface)
        internal set

    var surfaceVariant by mutableStateOf(values.surfaceVariant)
        internal set

    var onSurfaceVariant by mutableStateOf(values.onSurfaceVariant)
        internal set

    var surfaceContainer by mutableStateOf(values.surfaceContainer)
        internal set

    var surfaceContainerLow by mutableStateOf(values.surfaceContainerLow)
        internal set

    var surfaceContainerLowest by mutableStateOf(values.surfaceContainerLowest)
        internal set

    var surfaceContainerHigh by mutableStateOf(values.surfaceContainerHigh)
        internal set

    var surfaceContainerHighest by mutableStateOf(values.surfaceContainerHighest)
        internal set

    var surfaceDim by mutableStateOf(values.surfaceDim)
        internal set

    var surfaceBright by mutableStateOf(values.surfaceBright)
        internal set

    var inverseSurface by mutableStateOf(values.inverseSurface)
        internal set

    var inverseOnSurface by mutableStateOf(values.inverseOnSurface)
        internal set

    var inversePrimary by mutableStateOf(values.inversePrimary)
        internal set

    var outline by mutableStateOf(values.outline)
        internal set

    var outlineVariant by mutableStateOf(values.outlineVariant)
        internal set

    var scrim by mutableStateOf(values.scrim)
        internal set

    var error by mutableStateOf(values.error)
        internal set

    var onError by mutableStateOf(values.onError)
        internal set

    var errorContainer by mutableStateOf(values.errorContainer)
        internal set

    var onErrorContainer by mutableStateOf(values.onErrorContainer)
        internal set

    var background by mutableStateOf(values.background)
        internal set

    var onBackground by mutableStateOf(values.onBackground)
        internal set

    /**
     * Convert the current mutable state to immutable values.
     */
    fun toValues(): AppColorValues =
        AppColorValues(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondaryContainer,
            onSecondaryContainer = onSecondaryContainer,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiaryContainer,
            onTertiaryContainer = onTertiaryContainer,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = onSurfaceVariant,
            surfaceContainer = surfaceContainer,
            surfaceContainerLow = surfaceContainerLow,
            surfaceContainerLowest = surfaceContainerLowest,
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
            surfaceDim = surfaceDim,
            surfaceBright = surfaceBright,
            inverseSurface = inverseSurface,
            inverseOnSurface = inverseOnSurface,
            inversePrimary = inversePrimary,
            outline = outline,
            outlineVariant = outlineVariant,
            scrim = scrim,
            error = error,
            onError = onError,
            errorContainer = errorContainer,
            onErrorContainer = onErrorContainer,
            background = background,
            onBackground = onBackground,
        )

    /**
     * Create a copy of this color scheme.
     */
    fun copy(): AppColorScheme = AppColorScheme(toValues())

    /**
     * Update this color scheme from another AppColorScheme.
     */
    fun updateFrom(other: AppColorScheme) {
        primary = other.primary
        onPrimary = other.onPrimary
        primaryContainer = other.primaryContainer
        onPrimaryContainer = other.onPrimaryContainer
        secondary = other.secondary
        onSecondary = other.onSecondary
        secondaryContainer = other.secondaryContainer
        onSecondaryContainer = other.onSecondaryContainer
        tertiary = other.tertiary
        onTertiary = other.onTertiary
        tertiaryContainer = other.tertiaryContainer
        onTertiaryContainer = other.onTertiaryContainer
        surface = surface
        onSurface = other.onSurface
        surfaceVariant = other.surfaceVariant
        onSurfaceVariant = other.onSurfaceVariant
        surfaceContainer = other.surfaceContainer
        surfaceContainerLow = other.surfaceContainerLow
        surfaceContainerLowest = other.surfaceContainerLowest
        surfaceContainerHigh = other.surfaceContainerHigh
        surfaceContainerHighest = other.surfaceContainerHighest
        surfaceDim = other.surfaceDim
        surfaceBright = other.surfaceBright
        inverseSurface = other.inverseSurface
        inverseOnSurface = other.inverseOnSurface
        inversePrimary = other.inversePrimary
        outline = other.outline
        outlineVariant = other.outlineVariant
        scrim = other.scrim
        error = other.error
        onError = other.onError
        errorContainer = other.errorContainer
        onErrorContainer = other.onErrorContainer
        background = other.background
        onBackground = other.onBackground
    }

    /**
     * Update this color scheme from AppColorValues.
     */
    fun updateFrom(values: AppColorValues) {
        primary = values.primary
        onPrimary = values.onPrimary
        primaryContainer = values.primaryContainer
        onPrimaryContainer = values.onPrimaryContainer
        secondary = values.secondary
        onSecondary = values.onSecondary
        secondaryContainer = values.secondaryContainer
        onSecondaryContainer = values.onSecondaryContainer
        tertiary = values.tertiary
        onTertiary = values.onTertiary
        tertiaryContainer = values.tertiaryContainer
        onTertiaryContainer = values.onTertiaryContainer
        surface = values.surface
        onSurface = values.onSurface
        surfaceVariant = values.surfaceVariant
        onSurfaceVariant = values.onSurfaceVariant
        surfaceContainer = values.surfaceContainer
        surfaceContainerLow = values.surfaceContainerLow
        surfaceContainerLowest = values.surfaceContainerLowest
        surfaceContainerHigh = values.surfaceContainerHigh
        surfaceContainerHighest = values.surfaceContainerHighest
        surfaceDim = values.surfaceDim
        surfaceBright = values.surfaceBright
        inverseSurface = values.inverseSurface
        inverseOnSurface = values.inverseOnSurface
        inversePrimary = values.inversePrimary
        outline = values.outline
        outlineVariant = values.outlineVariant
        scrim = values.scrim
        error = values.error
        onError = values.onError
        errorContainer = values.errorContainer
        onErrorContainer = values.onErrorContainer
        background = values.background
        onBackground = values.onBackground
    }

    /**
     * Linearly interpolate between this and another color scheme.
     */
    fun lerp(to: AppColorScheme, fraction: Float): AppColorScheme {
        return AppColorScheme(
            AppColorValues(
                primary = lerp(primary, to.primary, fraction),
                onPrimary = lerp(onPrimary, to.onPrimary, fraction),
                primaryContainer = lerp(primaryContainer, to.primaryContainer, fraction),
                onPrimaryContainer = lerp(onPrimaryContainer, to.onPrimaryContainer, fraction),
                secondary = lerp(secondary, to.secondary, fraction),
                onSecondary = lerp(onSecondary, to.onSecondary, fraction),
                secondaryContainer = lerp(secondaryContainer, to.secondaryContainer, fraction),
                onSecondaryContainer = lerp(onSecondaryContainer, to.onSecondaryContainer, fraction),
                tertiary = lerp(tertiary, to.tertiary, fraction),
                onTertiary = lerp(onTertiary, to.onTertiary, fraction),
                tertiaryContainer = lerp(tertiaryContainer, to.tertiaryContainer, fraction),
                onTertiaryContainer = lerp(onTertiaryContainer, to.onTertiaryContainer, fraction),
                surface = lerp(surface, to.surface, fraction),
                onSurface = lerp(onSurface, to.onSurface, fraction),
                surfaceVariant = lerp(surfaceVariant, to.surfaceVariant, fraction),
                onSurfaceVariant = lerp(onSurfaceVariant, to.onSurfaceVariant, fraction),
                surfaceContainer = lerp(surfaceContainer, to.surfaceContainer, fraction),
                surfaceContainerLow = lerp(surfaceContainerLow, to.surfaceContainerLow, fraction),
                surfaceContainerLowest = lerp(surfaceContainerLowest, to.surfaceContainerLowest, fraction),
                surfaceContainerHigh = lerp(surfaceContainerHigh, to.surfaceContainerHigh, fraction),
                surfaceContainerHighest = lerp(surfaceContainerHighest, to.surfaceContainerHighest, fraction),
                surfaceDim = lerp(surfaceDim, to.surfaceDim, fraction),
                surfaceBright = lerp(surfaceBright, to.surfaceBright, fraction),
                inverseSurface = lerp(inverseSurface, to.inverseSurface, fraction),
                inverseOnSurface = lerp(inverseOnSurface, to.inverseOnSurface, fraction),
                inversePrimary = lerp(inversePrimary, to.inversePrimary, fraction),
                outline = lerp(outline, to.outline, fraction),
                outlineVariant = lerp(outlineVariant, to.outlineVariant, fraction),
                scrim = lerp(scrim, to.scrim, fraction),
                error = lerp(error, to.error, fraction),
                onError = lerp(onError, to.onError, fraction),
                errorContainer = lerp(errorContainer, to.errorContainer, fraction),
                onErrorContainer = lerp(onErrorContainer, to.onErrorContainer, fraction),
                background = lerp(background, to.background, fraction),
                onBackground = lerp(onBackground, to.onBackground, fraction),
            )
        )
    }
}

/**
 * Light theme color scheme.
 */
fun lightAppColorScheme(): AppColorValues {
    return AppColorValues(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
        surfaceDim = surfaceDimLight,
        surfaceBright = surfaceBrightLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        inversePrimary = inversePrimaryLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        scrim = scrimLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
    )
}

/**
 * Dark theme color scheme.
 */
fun darkAppColorScheme(): AppColorValues {
    return AppColorValues(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
        surfaceDim = surfaceDimDark,
        surfaceBright = surfaceBrightDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        inversePrimary = inversePrimaryDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        scrim = scrimDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
    )
}

/**
 * CompositionLocal for accessing the custom app color scheme.
 */
internal val LocalAppColorScheme = compositionLocalOf { AppColorScheme(darkAppColorScheme()) }
