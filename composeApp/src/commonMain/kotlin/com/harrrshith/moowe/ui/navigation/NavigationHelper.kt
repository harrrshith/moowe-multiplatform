package com.harrrshith.moowe.ui.navigation

import androidx.navigation.NavHostController

class NavigationHelper(
    private val navController: NavHostController
) {
    fun navigateToHome() {
        navController.navigate(Destination.Home.Discover){
            popUpTo(Destination.OnBoard::class.qualifiedName!!){
                inclusive = true
            }
        }
    }
}