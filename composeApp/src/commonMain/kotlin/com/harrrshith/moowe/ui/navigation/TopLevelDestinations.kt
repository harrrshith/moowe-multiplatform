package com.harrrshith.moowe.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.harrrshith.moowe.ui.components.composeVectors.DiscoverIcon
import com.harrrshith.moowe.ui.components.composeVectors.ForYouIcon
import com.harrrshith.moowe.ui.components.composeVectors.SearchIcon
import com.harrrshith.moowe.ui.components.composeVectors.TrendingIcon

sealed class TopLevelDestination(val route: Destination, val name: String, val icon: ImageVector) {
    object Discover : TopLevelDestination(Destination.Home.Discover, "Discover", DiscoverIcon)
    object Trending : TopLevelDestination(Destination.Home.Trending, "Trending", TrendingIcon)
    object Search : TopLevelDestination(Destination.Home.Search, "Search", SearchIcon)
    object Yours : TopLevelDestination(Destination.Home.Yours, "Yours", ForYouIcon)
}

val topLevelDestinations: List<TopLevelDestination>
    get() = listOf(
        TopLevelDestination.Discover,
        TopLevelDestination.Trending,
        TopLevelDestination.Search,
        TopLevelDestination.Yours
    )
fun String?.isTopLevelDestination(): Boolean {
    return this in topLevelDestinations.map { it.route::class.qualifiedName } //this is the currentRoute or screen we are currently in
}