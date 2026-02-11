package com.harrrshith.moowe.ui.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.ui.components.composeVectors.ArrowBackIcon
import com.harrrshith.moowe.ui.components.composeVectors.LikeIcon
import com.harrrshith.moowe.ui.components.composeVectors.ShareIcon
import com.harrrshith.moowe.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    title: String,
    alpha: Float,
    onBackPressed: () -> Unit,
    onLikeClicked: (Int) -> Unit,
    onShareClicked: (Int) -> Unit
) {
    val iconBackgroundAlpha = (1f - alpha).coerceIn(0f, 0.7f)
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colorScheme.surface.copy(alpha),
        ),
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBackPressed) {
                    Image(
                        imageVector = ArrowBackIcon,
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )
                }
            }
        },
        title = {
            if (alpha > 0.7f) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    color = AppTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onLikeClicked(0) }) {
                    Image(
                        imageVector = LikeIcon,
                        contentDescription = "Like",
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onShareClicked(0) }) {
                    Image(
                        imageVector = ShareIcon,
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )
                }
            }
        }
    )
}