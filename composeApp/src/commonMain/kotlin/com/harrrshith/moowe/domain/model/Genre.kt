package com.harrrshith.moowe.domain.model

enum class Genre(val id: Int, val displayName: String, val tvId: Int? = null) {
    TRENDING(0, "Trending", 0),
    ACTION(28, "Action & Adventure", 10759), // TV uses Action & Adventure combined
    ADVENTURE(12, "Adventure", 10759), // TV uses Action & Adventure combined
    FANTASY(14, "Sci-Fi & Fantasy", 10765), // TV uses Sci-Fi & Fantasy combined
    DOCUMENTARY(99, "Biopics & Documentaries", 99); // Same for both
    
    fun getIdForMediaType(mediaType: MediaType): Int {
        return when (mediaType) {
            MediaType.TV_SERIES -> tvId ?: id
            MediaType.MOVIE -> id
        }
    }
}