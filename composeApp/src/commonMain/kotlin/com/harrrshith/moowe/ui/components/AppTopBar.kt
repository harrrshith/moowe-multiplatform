package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
internal fun AppTopBar(
    title: String,
    hazeState: HazeState,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thin()){
                progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0.1f)
            }
            .fillMaxWidth(),
        title = {
            Text(
                text = title,
                style = AppTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}