package com.nammamela.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.nammamela.app.ui.screens.*

@Composable
fun NammaMelaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { 
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(Screen.SignUp.route) },
                onAdminLoginClick = { navController.navigate(Screen.AdminLogin.route) }
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { 
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onClose = { navController.popBackStack() }
            )
        }

        // --- NEW ADMIN AUTH ROUTES ---
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.AdminDashboard.route) {
                        popUpTo(Screen.AdminLogin.route) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(Screen.AdminSignUp.route) },
                onBackToFanLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminSignUp.route) {
            AdminSignUpScreen(
                onSignUpClick = {
                    navController.navigate(Screen.AdminLogin.route) {
                        popUpTo(Screen.AdminSignUp.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.AdminLogin.route) },
                onClose = { navController.popBackStack() }
            )
        }
        // -----------------------------

        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToSeatBooking = { playId -> 
                    navController.navigate(Screen.SeatBooking.createRoute(playId))
                },
                onNavigateToPlayDetail = { playId ->
                    navController.navigate(Screen.PlayDetail.createRoute(playId))
                },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onMyBookingsClick = { navController.navigate(Screen.MyBookings.route) },
                onAdminDashboardClick = { navController.navigate(Screen.AdminDashboard.route) },
                onLogoutClick = { 
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.PlayDetail.route,
            arguments = listOf(navArgument("playId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playId = backStackEntry.arguments?.getInt("playId") ?: 0
            PlayDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onBookClick = { navController.navigate(Screen.SeatBooking.createRoute(playId)) },
                onReviewClick = { navController.navigate(Screen.Review.createRoute(playId)) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayDetail = { playId ->
                    navController.navigate(Screen.PlayDetail.createRoute(playId))
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.Review.route,
            arguments = listOf(navArgument("playId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playId = backStackEntry.arguments?.getInt("playId") ?: 0
            ReviewScreen(
                playId = playId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.TicketConfirmation.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getInt("bookingId") ?: 0
            TicketConfirmationScreen(
                bookingId = bookingId,
                onNavigateHome = { 
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.SeatBooking.route,
            arguments = listOf(navArgument("playId") { type = NavType.IntType })
        ) {
            SeatBookingScreen(
                onNavigateBack = { navController.popBackStack() },
                onBookingSuccess = { bookingId -> 
                    navController.navigate(Screen.TicketConfirmation.createRoute(bookingId)) {
                        popUpTo(Screen.SeatBooking.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.MyBookings.route) {
            MyBookingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onTicketClick = { bookingId -> 
                    navController.navigate(Screen.TicketConfirmation.createRoute(bookingId))
                }
            )
        }
        
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onNavigateToUploadPlay = { navController.navigate(Screen.UploadPlay.route) },
                onNavigateToManageCast = { playId -> navController.navigate(Screen.ManageCast.createRoute(playId)) },
                onNavigateToManageSeats = { playId -> navController.navigate(Screen.ManageSeats.createRoute(playId)) },
                onNavigateToInsights = { navController.navigate(Screen.ManagerInsights.route) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ManagerInsights.route) {
            ManagerDashboardScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.UploadPlay.route) {
            AdminPlayManagementScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.ManageSeats.route,
            arguments = listOf(navArgument("playId") { type = NavType.IntType })
        ) {
            AdminSeatControlScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ManageCast.route,
            arguments = listOf(navArgument("playId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playId = backStackEntry.arguments?.getInt("playId") ?: 0
            ManageCastScreen(
                playId = playId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
