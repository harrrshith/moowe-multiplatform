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
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format
import com.harrrshith.moowe.utils.posterUrl

fun LazyListScope.detailRelatedMovies(
    modifier: Modifier = Modifier,
    relatedMovies: List<Movie>,
    mediaType: MediaType,
) {
    if (relatedMovies.isEmpty()) return

    item(key = "related_movies_section") {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = if (mediaType == MediaType.TV_SERIES) "related series" else "related movies",
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = relatedMovies, key = { it.id }) { movie ->
                    RelatedMovieCard(movie = movie)
                }
            }
        }
    }
}

@Composable
private fun RelatedMovieCard(movie: Movie) {
    Column(modifier = Modifier.width(132.dp)) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(posterUrl(movie.posterPath, size = "w342")),
                contentDescription = movie.title,
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
                text = movie.voteAverage.format(1),
                style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AppTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.title,
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AppTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = movie.releaseDate.take(4),
            style = AppTheme.typography.labelSmall,
            color = AppTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}
