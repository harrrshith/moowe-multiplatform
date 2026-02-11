package com.harrrshith.moowe.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format

fun LazyListScope.detailLineTwo(
    movie : Movie
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colorScheme.surface)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "★",
                            style = AppTheme.typography.titleMedium,
                            color = AppTheme.colorScheme.primary
                        )
                        Text(
                            text = movie.voteAverage.format(1),
                            style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = movie.releaseDate.take(4), // Just the year
                    style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = AppTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${movie.voteCount} reviews",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}