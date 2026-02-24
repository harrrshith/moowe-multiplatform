package com.harrrshith.moowe.data.local.entity

import androidx.room.Entity

@Entity(tableName = "recent_searches", primaryKeys = ["cacheKey"])
data class RecentSearchEntity(
    val cacheKey: String,
    val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val mediaType: String,
    val searchedAt: Long,
)
