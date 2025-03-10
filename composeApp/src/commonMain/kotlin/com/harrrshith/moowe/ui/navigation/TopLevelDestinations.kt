package com.harrrshith.moowe.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelDestination(val route: Destination, val name: String, val icon: ImageVector) {
    object Discover : TopLevelDestination(Destination.Home.Discover, "Discover", Icons.Filled.Home)
    object Trending : TopLevelDestination(Destination.Home.Trending, "Trending", Icons.Filled.Star)
    object Search : TopLevelDestination(Destination.Home.Search, "Search", Icons.Filled.Search)
    object Yours : TopLevelDestination(Destination.Home.Yours, "Yours", Icons.Filled.Person)
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