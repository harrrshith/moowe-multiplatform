package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.ImageCard

fun LazyListScope.trendingList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
){
    item {
        ImageCarousel(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = movies) { movie ->
                ImageCard(
                    modifier = Modifier
                        .aspectRatio(1.5f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    imageUrl = movie.backdropPath,
                    movieTitle = movie.title,
                    onClick = { onMovieClick(movie) },
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
) {
    item {
        val actualItemWidth = remember { screenWidth / (itemsTobeDisplayed + 0.5f) }
        val horizontalPadding = remember { screenWidth - ((actualItemWidth + 8.dp) * itemsTobeDisplayed) }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = horizontalPadding),
                text = genre.displayName,
                style = MaterialTheme.typography.headlineMedium,
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                state = lazyListState,
                contentPadding = PaddingValues(start = horizontalPadding, end = horizontalPadding),
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