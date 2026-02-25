package com.harrrshith.moowe.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.harrrshith.moowe.ui.components.composeVectors.DiscoverIcon
import com.harrrshith.moowe.ui.components.composeVectors.ForYouIcon
import com.harrrshith.moowe.ui.components.composeVectors.SearchIcon
import com.harrrshith.moowe.ui.components.composeVectors.TrendingIcon

sealed class TopLevelDestination(val key: Destination, val name: String, val icon: ImageVector) {
    data object Discover : TopLevelDestination(Destination.Home.Discover, "Discover", DiscoverIcon)
    data object Trending : TopLevelDestination(Destination.Home.Trending, "Trending", TrendingIcon)
    data object Search : TopLevelDestination(Destination.Home.Search, "Search", SearchIcon)
    data object Yours : TopLevelDestination(Destination.Home.Yours, "Yours", ForYouIcon)
}

val topLevelDestinations: List<TopLevelDestination>
    get() = listOf(
        TopLevelDestination.Discover,
        TopLevelDestination.Trending,
        TopLevelDestination.Search,
        TopLevelDestination.Yours
    )

fun Destination.isTopLevelDestination(): Boolean {
    return topLevelDestinations.any { it.key == this }
}
