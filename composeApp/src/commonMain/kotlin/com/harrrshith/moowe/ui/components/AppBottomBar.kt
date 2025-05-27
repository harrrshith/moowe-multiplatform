package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.harrrshith.moowe.ui.navigation.Destination
import com.harrrshith.moowe.ui.navigation.TopLevelDestination
import com.harrrshith.moowe.ui.navigation.isTopLevelDestination
import com.harrrshith.moowe.ui.navigation.topLevelDestinations

@Composable
fun AppBottomBar(navController: NavHostController){
    val tabs = remember { topLevelDestinations }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    if (currentDestination.isTopLevelDestination()) {
        BottomBar(
            tabs = tabs,
            currentDestination = currentDestination,
            onTabItemClick = { tab ->
                if (tab.route::class.qualifiedName != currentDestination) {
                    navController.navigate(tab.route) {
                        popUpTo(Destination.Home.Discover::class.qualifiedName!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}

@Composable
private fun BottomBar(
    tabs: List<TopLevelDestination>,
    currentDestination: String?,
    onTabItemClick: (TopLevelDestination) -> Unit
){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
    ){
        tabs.forEach { tab ->
            val currentSelectedTab = currentDestination == tab.route::class.qualifiedName
            NavigationBarItem(
                modifier = Modifier.scale(
                    if (currentSelectedTab) 1.15f else 1f
                ),
                selected = currentSelectedTab,
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.name,
                    )
                },
                onClick = {
                    onTabItemClick(tab)
                },
                colors =  NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = .2f)
                )
            )
        }
    }
}