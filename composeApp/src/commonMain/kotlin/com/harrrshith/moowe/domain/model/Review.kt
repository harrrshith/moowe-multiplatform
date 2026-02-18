package com.harrrshith.moowe.domain.model

data class Review(
    val id: String,
    val author: String,
    val authorUsername: String,
    val avatarPath: String?,
    val rating: Double?,
    val content: String,
    val createdAt: String,
)
