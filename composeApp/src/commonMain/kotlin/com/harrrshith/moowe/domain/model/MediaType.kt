package com.harrrshith.moowe.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MediaType(val displayName: String, val apiValue: String) {
    MOVIE("Movies", "movie"),
    TV_SERIES("Series", "tv");

    companion object {
        fun fromApiValue(value: String): MediaType {
            return entries.firstOrNull { it.apiValue == value } ?: MOVIE
        }
    }
}
