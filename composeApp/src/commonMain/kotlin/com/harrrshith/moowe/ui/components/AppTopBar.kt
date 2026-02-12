package com.harrrshith.moowe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.ui.components.composeVectors.ArrowDropDownIcon
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
    selectedMediaType: MediaType? = null,
    onMediaTypeSelected: ((MediaType) -> Unit)? = null
) {
    var showDropdown by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thin()){
                progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0.1f)
            }
            .fillMaxWidth(),
        title = {
            if (selectedMediaType != null && onMediaTypeSelected != null) {
                Box {
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                showDropdown = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedMediaType.displayName,
                            style = AppTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Icon(
                            imageVector = ArrowDropDownIcon,
                            contentDescription = "Select media type",
                            modifier = Modifier.size(28.dp),
                            tint = AppTheme.colorScheme.onSurface
                        )
                    }
                    
                    StyledDropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        selectedMediaType = selectedMediaType,
                        onMediaTypeSelected = { mediaType ->
                            onMediaTypeSelected(mediaType)
                            showDropdown = false
                        }
                    )
                }
            } else {
                Text(
                    text = title,
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
private fun StyledDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedMediaType: MediaType,
    onMediaTypeSelected: (MediaType) -> Unit
) {
    if (expanded) {
        Popup(
            alignment = Alignment.TopCenter,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(250)) + scaleIn(
                    initialScale = 0.85f,
                    animationSpec = tween(250)
                ),
                exit = fadeOut(animationSpec = tween(200)) + scaleOut(
                    targetScale = 0.85f,
                    animationSpec = tween(200)
                )
            ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 60.dp),
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 16.dp,
                    tonalElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        // Background with gradient and blur effect
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            AppTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.98f),
                                            AppTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.96f),
                                            AppTheme.colorScheme.surfaceContainer.copy(alpha = 0.94f)
                                        )
                                    )
                                )
                        )
                        
                        // Accent glow at top
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            AppTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                        
                        Column(
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            MediaType.entries.forEach { mediaType ->
                                val isSelected = mediaType == selectedMediaType
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onMediaTypeSelected(mediaType) }
                                        .background(
                                            brush = if (isSelected) {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        AppTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                                        AppTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        AppTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                                    )
                                                )
                                            } else {
                                                Brush.linearGradient(
                                                    colors = listOf(Color.Transparent, Color.Transparent)
                                                )
                                            }
                                        )
                                        .padding(horizontal = 32.dp, vertical = 16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = mediaType.displayName,
                                            style = AppTheme.typography.titleLarge.copy(
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                letterSpacing = if (isSelected) 0.8.sp else 0.5.sp
                                            ),
                                            color = if (isSelected) {
                                                AppTheme.colorScheme.primary
                                            } else {
                                                AppTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                                            }
                                        )
                                        
                                        // Selection indicator dot
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .size(6.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(AppTheme.colorScheme.primary)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

