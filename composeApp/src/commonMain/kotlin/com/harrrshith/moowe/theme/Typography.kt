package com.harrrshith.moowe.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moowe.composeapp.generated.resources.Res
import moowe.composeapp.generated.resources.noto_sans_variable
import org.jetbrains.compose.resources.Font

val notoSansFont: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.Normal),
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.Black), //After adding this, black weight was available. No idea why it wasn't working then or is this desired behavior?

    )


@Composable
fun MooweTypography(): Typography {
    val defaultTypography = Typography()
    return defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
            letterSpacing = 0.4.sp,
        ),
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
        ),
        titleLarge = defaultTypography.titleLarge.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.Bold, // Added fontWeight for consistency
        ),
        bodyLarge = defaultTypography.bodyLarge.copy(
            fontFamily = notoSansFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        bodySmall = defaultTypography.bodySmall.copy(
            fontFamily = notoSansFont,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        ),
        labelLarge = defaultTypography.labelLarge.copy(
            fontFamily = notoSansFont,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        ),
        labelMedium = defaultTypography.labelMedium.copy(
            fontFamily = notoSansFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = notoSansFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        )
    )
}