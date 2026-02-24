package com.harrrshith.moowe.ui.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.domain.model.Season
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.posterUrl

fun LazyListScope.detailSeasons(
    modifier: Modifier = Modifier,
    seasons: List<Season>,
) {
    if (seasons.isEmpty()) return

    item(key = "seasons_section") {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "seasons",
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = seasons, key = { it.id }) { season ->
                    SeasonCard(season = season)
                }
            }
        }
    }
}

@Composable
private fun SeasonCard(season: Season) {
    Column(modifier = Modifier.width(164.dp)) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(posterUrl(season.posterPath, size = "w342")),
                contentDescription = season.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.72f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppTheme.colorScheme.surfaceVariant),
            )

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppTheme.colorScheme.surface.copy(alpha = 0.9f))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                text = "${season.episodeCount} eps",
                style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = season.name.ifBlank { "Season ${season.seasonNumber}" },
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AppTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = season.airDate.take(4).ifBlank { "TBA" },
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}
