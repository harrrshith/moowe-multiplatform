package com.harrrshith.moowe.ui.detail.mock

import com.harrrshith.moowe.domain.model.Movie

val mockMovie = Movie(
    id = 1,
    title = "Inception",
    overview = "A skilled thief is offered a chance to have his past crimes forgiven.",
    posterPath = "sample-poster1.jpg",
    backdropPath = "sample-backdrop1.jpg",
    releaseDate = "2010-07-16",
    voteAverage = 8.8,
    voteCount = 21000,
    popularity = 150.5,
    adult = false,
    genreIds = listOf(28, 12)
)