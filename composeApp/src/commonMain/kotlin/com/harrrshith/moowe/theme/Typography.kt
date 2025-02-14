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
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.Black),
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.ExtraBold),
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.Medium),
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.Normal),
        Font(resource = Res.font.noto_sans_variable, weight = FontWeight.ExtraLight),
    )


@Composable
fun MooweTypography(): Typography {
    val defaultTypography = Typography()
    return defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.Black,
            fontSize = 42.sp,
            letterSpacing = .4.sp,
        ),
        headlineLarge = defaultTypography.headlineLarge.copy(
            fontFamily = notoSansFont,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
        ),
        headlineMedium = defaultTypography.headlineMedium.copy(
            fontFamily = notoSansFont,
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
        labelMedium = defaultTypography.labelSmall.copy(
            fontFamily = notoSansFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        ),
        labelSmall = defaultTypography.labelSmall.copy(
            fontFamily = notoSansFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        ),
    )
}