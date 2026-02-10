package com.harrrshith.moowe.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moowe.composeapp.generated.resources.Inter_Bold
import moowe.composeapp.generated.resources.Inter_Medium
import moowe.composeapp.generated.resources.Inter_Regular
import moowe.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


val interFont: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = Res.font.Inter_Regular, weight = FontWeight.Normal),
        Font(resource = Res.font.Inter_Regular, weight = FontWeight.Light),
        Font(resource = Res.font.Inter_Medium, weight = FontWeight.Medium),
        Font(resource = Res.font.Inter_Medium, weight = FontWeight.SemiBold),
        Font(resource = Res.font.Inter_Bold, weight = FontWeight.Bold),
        Font(resource = Res.font.Inter_Bold, weight = FontWeight.Black),
    )

@Composable
fun appTypography(): Typography {
    val defaultTypography = Typography()
    return defaultTypography.copy(
        // Display styles (largest text)
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
            letterSpacing = 0.4.sp,
        ),
        displayMedium = defaultTypography.displayMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
        displaySmall = defaultTypography.displaySmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
        ),
        
        // Headline styles
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        ),
        headlineSmall = defaultTypography.headlineSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        ),
        
        // Title styles
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
        titleSmall = defaultTypography.titleSmall.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Medium,
        ),
        
        // Body styles (main content)
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = interFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        ),
        bodyMedium = defaultTypography.bodyMedium.copy(
            fontFamily = interFont,
            fontWeight = FontWeight.Normal,
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = interFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        ),
        
        // Label styles (smaller UI text)
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
            fontWeight = FontWeight.Normal,
        )
    )
}