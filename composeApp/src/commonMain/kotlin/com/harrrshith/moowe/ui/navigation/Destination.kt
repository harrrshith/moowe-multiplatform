package com.harrrshith.moowe.ui.navigation

import kotlinx.serialization.Serializable


sealed class Destination{
    @Serializable
    data object OnBoard: Destination()
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
}