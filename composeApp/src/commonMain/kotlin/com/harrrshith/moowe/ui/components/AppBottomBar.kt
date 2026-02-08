package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        Surface(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
            shape = RoundedCornerShape(42.dp)
        ){
            BottomBar(
                modifier = Modifier,
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
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun BottomBar(
    modifier: Modifier,
    hazeState: HazeState,
    tabs: List<TopLevelDestination>,
    currentDestination: String?,
    onTabItemClick: (TopLevelDestination) -> Unit
){
    NavigationBar(
        modifier = modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thick()
            ){
                progressive = HazeProgressive.verticalGradient(startIntensity = 0.5f, endIntensity = 1f)
            }
            .padding(horizontal = 12.dp),
        containerColor = Color.Transparent,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ){
        tabs.forEach { tab ->
            val currentSelectedTab = currentDestination == tab.route::class.qualifiedName
            NavigationBarItem(
                selected = currentSelectedTab,
                icon = {
                    Icon(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        imageVector = tab.icon,
                        contentDescription = tab.name,
                    )
                },
                onClick = { onTabItemClick(tab) },
                colors =  NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = .5f)
                )
            )
        }
    }
}