package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.ui.components.composeVectors.ArrowDropDownIcon
import com.harrrshith.moowe.ui.theme.AppTheme

@Composable
fun MediaTypeSwitcher(
    selectedMediaType: MediaType,
    onMediaTypeSelected: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        // Switcher Button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(AppTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedMediaType.displayName,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = ArrowDropDownIcon,
                contentDescription = "Expand",
                tint = AppTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(AppTheme.colorScheme.surface)
        ) {
            MediaType.entries.forEach { mediaType ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = mediaType.displayName,
                            style = AppTheme.typography.bodyLarge,
                            color = if (mediaType == selectedMediaType) {
                                AppTheme.colorScheme.primary
                            } else {
                                AppTheme.colorScheme.onSurface
                            }
                        )
                    },
                    onClick = {
                        onMediaTypeSelected(mediaType)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (mediaType == selectedMediaType) {
                            AppTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        } else {
                            Color.Transparent
                        }
                    )
                )
            }
        }
    }
}
