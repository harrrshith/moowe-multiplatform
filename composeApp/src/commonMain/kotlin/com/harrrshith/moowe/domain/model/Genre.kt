package com.harrrshith.moowe.domain.model

enum class Genre(val id: Int, val displayName: String, val tvId: Int? = null) {
    TRENDING(0, "Trending", 0),
    ACTION(28, "Action", 10759),
    ADVENTURE(12, "Adventure", 10759),
    ANIMATION(16, "Animation", 16),
    COMEDY(35, "Comedy", 35),
    CRIME(80, "Crime", 80),
    DOCUMENTARY(99, "Documentary", 99),
    DRAMA(18, "Drama", 18),
    FAMILY(10751, "Family", 10751),
    FANTASY(14, "Fantasy", 10765),
    HISTORY(36, "History", 36),
    HORROR(27, "Horror", 27),
    MYSTERY(9648, "Mystery", 9648),
    ROMANCE(10749, "Romance", 10749),
    SCIENCE_FICTION(878, "Science Fiction", 10765),
    THRILLER(53, "Thriller", 53);
    
    fun getIdForMediaType(mediaType: MediaType): Int {
        return when (mediaType) {
            MediaType.TV_SERIES -> tvId ?: id
            MediaType.MOVIE -> id
        }
    }

    companion object {
        val DiscoverFeedGenres = listOf(
            ACTION,
            ADVENTURE,
            ANIMATION,
            COMEDY,
            CRIME,
            DRAMA,
            FANTASY,
            SCIENCE_FICTION,
            MYSTERY,
            THRILLER,
            ROMANCE,
            FAMILY,
            HISTORY,
            HORROR,
            DOCUMENTARY,
        )
    }
}
