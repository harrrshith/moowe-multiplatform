package com.harrrshith.moowe.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import moowe.composeapp.generated.resources.Res
import moowe.composeapp.generated.resources.noto_sans_variable
import org.jetbrains.compose.resources.Font


val FontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(resource = Res.font.noto_sans_variable)
    )
