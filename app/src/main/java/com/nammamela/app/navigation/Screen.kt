package com.nammamela.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Main : Screen("main")
    object Home : Screen("home")
    object SeatBooking : Screen("seat_booking/{playId}") {
        fun createRoute(playId: Int) = "seat_booking/$playId"
    }
    object Cast : Screen("cast")
    object FanWall : Screen("fan_wall")
    object Profile : Screen("profile")
    object MyBookings : Screen("my_bookings")
    object Notifications : Screen("notifications")
    object Search : Screen("search")
    object PlayDetail : Screen("play_detail/{playId}") {
        fun createRoute(playId: Int) = "play_detail/$playId"
    }
    object Review : Screen("review/{playId}") {
        fun createRoute(playId: Int) = "review/$playId"
    }
    
    object TicketConfirmation : Screen("ticket_confirmation/{bookingId}") {
        fun createRoute(bookingId: Int) = "ticket_confirmation/$bookingId"
    }
    
    // Admin Routes
    object AdminLogin : Screen("admin_login")
    object AdminSignUp : Screen("admin_signup")
    object AdminDashboard : Screen("admin_dashboard")
    object UploadPlay : Screen("upload_play")
    object ManageCast : Screen("manage_cast/{playId}") {
        fun createRoute(playId: Int) = "manage_cast/$playId"
    }
    object ManageSeats : Screen("manage_seats/{playId}") {
        fun createRoute(playId: Int) = "manage_seats/$playId"
    }
    object ManagerInsights : Screen("manager_insights")
}
