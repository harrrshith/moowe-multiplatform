package com.harrrshith.moowe.ui.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format
import com.harrrshith.moowe.utils.extensions.withCommas

fun LazyListScope.detailLineTwo(
    modifier: Modifier = Modifier,
    movie : Movie
) {
    item {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    RatingIndicator(
                        rating = movie.voteAverage
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = movie.releaseDate,
                    style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = AppTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${movie.voteCount.withCommas()} reviews",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RatingIndicator(
    modifier: Modifier = Modifier,
    rating: Double,
    maxRating: Double = 10.0,
    strokeWidth: Dp = 4.dp,
    size: Dp = 32.dp
) {
    val progress = remember(rating, maxRating) {
        (rating / maxRating).coerceIn(0.0, 1.0).toFloat()
    }

    val displayProgress = progress.coerceAtLeast(0.01f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)

    ) {
        CircularProgressIndicator(
            progress = { displayProgress },
            strokeWidth = strokeWidth,
            color = AppTheme.colorScheme.primary,
            trackColor = AppTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.05f)
        )

        Text(
            text = rating.format(1),
            style = AppTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            color = AppTheme.colorScheme.onSurfaceVariant
        )
    }
}
