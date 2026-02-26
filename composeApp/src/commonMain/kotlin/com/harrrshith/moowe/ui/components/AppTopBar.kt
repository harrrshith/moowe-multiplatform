package com.harrrshith.moowe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.ui.components.composeVectors.SearchIcon
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
internal fun SegmentedAppTopBar(
    hazeState: HazeState,
    selectedMediaType: MediaType = MediaType.MOVIE,
    onMediaTypeSelected: (MediaType) -> Unit,
    onSearchClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thin()
            ) {
                progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0.1f)
            }
            .fillMaxWidth(),
        title = {
            MediaTypeSegmentedControl(
                selectedMediaType = selectedMediaType,
                onMediaTypeSelected = onMediaTypeSelected
            )
        },
        actions = {
            if (onSearchClick != null) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = SearchIcon,
                        contentDescription = "Search",
                        tint = AppTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

private val SegmentWidth = 80.dp
private val ControlHeight = 36.dp
private val TrackPadding = 3.dp

@Composable
private fun MediaTypeSegmentedControl(
    selectedMediaType: MediaType,
    onMediaTypeSelected: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = MediaType.entries
    val selectedIndex = options.indexOf(selectedMediaType)

    val pillOffset by animateDpAsState(
        targetValue = SegmentWidth * selectedIndex,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pill_offset"
    )

    Box(
        modifier = modifier
            .width(SegmentWidth * options.size + TrackPadding * 2)
            .height(ControlHeight)
            .clip(RoundedCornerShape(50))
            .background(AppTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.82f))
            .padding(TrackPadding)
    ) {
        // Sliding pill
        Box(
            modifier = Modifier
                .offset(x = pillOffset)
                .width(SegmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AppTheme.colorScheme.primary,
                            AppTheme.colorScheme.secondary,
                        )
                    )
                )
        )

        // Labels — drawn on top of the pill so they're always readable
        Row(modifier = Modifier.fillMaxSize()) {
            options.forEach { mediaType ->
                val isSelected = mediaType == selectedMediaType
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) AppTheme.colorScheme.onPrimary
                    else AppTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 200),
                    label = "text_color_${mediaType.name}"
                )
                Box(
                    modifier = Modifier
                        .width(SegmentWidth)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onMediaTypeSelected(mediaType) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mediaType.displayName,
                        style = AppTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 0.3.sp
                        ),
                        color = textColor
                    )
                }
            }
        }
    }
}
