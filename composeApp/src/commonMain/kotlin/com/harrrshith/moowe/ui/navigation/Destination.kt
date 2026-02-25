package com.harrrshith.moowe.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
sealed class Destination : NavKey {
    @Serializable
    data object Home: Destination() {
        @Serializable
        data object Discover: Destination()
        @Serializable
        data object Trending: Destination()
        @Serializable
        data object Search: Destination()
        @Serializable
        data object Yours: Destination()
    }
    @Serializable
    data class Detail(
        val id: Int,
        val mediaType: String,
        val sharedKey: String,
        val title: String = "",
        val posterPath: String = "",
    ): Destination()
}
