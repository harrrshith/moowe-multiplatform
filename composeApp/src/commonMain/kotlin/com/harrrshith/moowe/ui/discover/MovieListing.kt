package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.ImageCard
import com.harrrshith.moowe.ui.theme.AppTheme

fun LazyListScope.trendingList(
    movies: List<Movie>,
    onClick: (Int) -> Unit,
) {
    item {
        val itemWidthFraction = 0.75f
        val carouselWidth = screenWidth
        val sidePadding = (carouselWidth - (carouselWidth * itemWidthFraction)) / 2
        val density = LocalDensity.current
        val sidePaddingPx = with(density) { sidePadding.roundToPx() }

        val infiniteItemCount = Int.MAX_VALUE
        val startIndex = infiniteItemCount / 2
        val carouselState = rememberLazyListState(
            initialFirstVisibleItemIndex = startIndex,
            initialFirstVisibleItemScrollOffset = -sidePaddingPx
        )
        
        ImageCarousel(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            state = carouselState,
            itemHeight = screenWidth * 0.5f,
            itemWidthFraction = itemWidthFraction,
            spacing = 32.dp,
        ) {
            items(count = infiniteItemCount) { index ->
                val movieIndex = index % movies.size
                val movie = movies[movieIndex]
                ImageCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp)),
                    imageUrl = movie.backdropPath,
                    movieTitle = movie.title,
                    onClick = { onClick(movie.id) },
                )
            }
        }
    }
}

fun LazyListScope.movieList(
    genre: Genre,
    movies: List<Movie>,
    lazyListState: LazyListState,
    itemsTobeDisplayed: Int,
    screenWidth: Dp,
    onClick: (Int) -> Unit = {}
) {
    item {
        val actualItemWidth = remember { screenWidth / (itemsTobeDisplayed + 0.5f) }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = genre.displayName,
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                state = lazyListState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = movies,
                    key = {movie -> movie.id }
                ) { movie ->
                    Box(
                        modifier = Modifier
                            .width(actualItemWidth)
                            .aspectRatio(0.75f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable{
                                onClick(movie.id)
                            }
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.5f)),
                            painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.posterPath}"),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }

                }
            }
        }
    }
}
