package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.harrrshith.moowe.ui.navigation.Destination
import com.harrrshith.moowe.ui.navigation.TopLevelDestination
import com.harrrshith.moowe.ui.navigation.isTopLevelDestination
import com.harrrshith.moowe.ui.navigation.topLevelDestinations
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
fun AppBottomBar(
    navController: NavHostController,
    hazeState: HazeState
){
    val tabs = remember { topLevelDestinations }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    if (currentDestination.isTopLevelDestination()) {
        BottomBar(
            hazeState = hazeState,
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

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun BottomBar(
    hazeState: HazeState,
    tabs: List<TopLevelDestination>,
    currentDestination: String?,
    onTabItemClick: (TopLevelDestination) -> Unit
){
    NavigationBar(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thick()){
                progressive = HazeProgressive.verticalGradient(startIntensity = 0.5f, endIntensity = 1f)
            }
            .fillMaxWidth(),
        containerColor = Color.Transparent
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