package com.harrrshith.moowe.utils

private const val TMDB_IMAGE_BASE = "https://image.tmdb.org/t/p"

fun posterUrl(path: String?, size: String = "w780"): String {
    if (path.isNullOrBlank()) return ""
    val normalizedPath = path.trimStart('/')
    return "$TMDB_IMAGE_BASE/$size/$normalizedPath"
}

fun profileUrl(path: String?, size: String = "w185"): String {
    if (path.isNullOrBlank()) return ""
    val normalizedPath = path.trimStart('/')
    return "$TMDB_IMAGE_BASE/$size/$normalizedPath"
}
