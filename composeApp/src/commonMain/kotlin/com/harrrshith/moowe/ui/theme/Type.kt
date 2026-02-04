package com.harrrshith.moowe.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val interFont: FontFamily
    @Composable
    get() = FontFamily.Default // Temporarily using system font until iOS resources are configured
    // TODO: Re-enable custom font once iOS resource bundling is fixed
    // FontFamily(Font(resource = Res.font.inter_variable_font, weight = FontWeight.Normal))

@Composable
fun AppTypography(): Typography {
    val defaultTypography = Typography()
    return defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
            letterSpacing = 0.4.sp,
        ),
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
        ),
        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        titleMedium = defaultTypography.titleMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold, // Added fontWeight for consistency
            fontSize = 20.sp,
        ),
        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        ),
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = interFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = interFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        ),
        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = interFont,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = interFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = interFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        )
    )
}