package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nammamela.app.navigation.Screen
import com.nammamela.app.ui.theme.*

sealed class BottomNavItem(val screen: Screen, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Screen.Home, Icons.Default.Home, "Home")
    object Cast : BottomNavItem(Screen.Cast, Icons.Default.Group, "Cast")
    object FanWall : BottomNavItem(Screen.FanWall, Icons.Default.QuestionAnswer, "Fan Wall")
    object Profile : BottomNavItem(Screen.Profile, Icons.Default.Person, "Profile")
}

@Composable
fun MainScreen(
    onNavigateToSeatBooking: (Int) -> Unit,
    onNavigateToPlayDetail: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onMyBookingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = NammaDarkBrown,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = NammaSurfaceLow.copy(0.98f),
                tonalElevation = 0.dp, // Flat look as requested
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.height(80.dp).windowInsetsPadding(WindowInsets.navigationBars),
                    tonalElevation = 0.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Cast,
                        BottomNavItem.FanWall,
                        BottomNavItem.Profile
                    )
                    
                    for (item in items) {
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
                        
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    item.icon, 
                                    contentDescription = item.label,
                                    modifier = Modifier.size(24.dp)
                                ) 
                            },
                            label = { 
                                Text(
                                    item.label, 
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                ) 
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NammaGold, // Now Radiant Amber
                                selectedTextColor = NammaGold,
                                unselectedIconColor = NammaWarmWhite.copy(0.3f),
                                unselectedTextColor = NammaWarmWhite.copy(0.3f),
                                indicatorColor = Color.Transparent // Removed the circular highlight/design
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToSeatBooking = onNavigateToSeatBooking,
                    onNavigateToPlayDetail = onNavigateToPlayDetail,
                    onNavigateToSearch = onNavigateToSearch,
                    onNavigateToNotifications = onNavigateToNotifications
                )
            }
            composable(Screen.Cast.route) {
                CastScreen()
            }
            composable(Screen.FanWall.route) {
                FanWallScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onMyBookingsClick = onMyBookingsClick,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}
