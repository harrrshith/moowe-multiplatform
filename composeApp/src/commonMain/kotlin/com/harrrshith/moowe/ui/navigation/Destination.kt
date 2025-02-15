package com.harrrshith.moowe.ui.navigation

import kotlinx.serialization.Serializable


sealed class Destination{
    @Serializable
    data object OnBoard: Destination()
    @Serializable
    sealed class Home: Destination() {
        @Serializable
        data object Discover: Home()
        @Serializable
        data object Trending: Home()
        @Serializable
        data object Search: Home()
        @Serializable
        data object Yours: Home()
    }
}