package com.nammamela.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onMyBookingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val bookings by viewModel.bookings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaDarkBrown)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Theatrical Header Background (Cooler Gradient)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NammaDeepMaroon.copy(0.5f), NammaDarkBrown)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Profile Avatar (Clean Frost Border)
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.05f))
                            .border(2.dp, NammaGold, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Surface(
                        color = NammaGold,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.BottomEnd)
                            .border(2.dp, NammaDarkBrown, CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Edit, null, tint = NammaDarkBrown, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = user?.name ?: "Basavaraj Patil",
                    color = NammaWarmWhite,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = user?.email ?: "basu@nammamela.com",
                    color = NammaGold.copy(0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Stats Row
        Surface(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-40).dp),
            color = NammaSurfaceLow,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat("Bookings", "${bookings.size}")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = NammaWarmWhite.copy(0.1f))
                ProfileStat("Reviews", "12")
                Divider(modifier = Modifier.height(40.dp).width(1.dp), color = NammaWarmWhite.copy(0.1f))
                ProfileStat("Favorites", "4")
            }
        }

        // Main Menu
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                "ACCOUNT SETTINGS", 
                color = NammaWarmWhite.copy(0.4f), 
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
            )
            
            Surface(
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, NammaWarmWhite.copy(0.03f))
            ) {
                Column {
                    ProfileMenuItem(Icons.Outlined.ConfirmationNumber, "My Bookings", "View all your past and upcoming shows") { onMyBookingsClick() }
                    Divider(modifier = Modifier.padding(horizontal = 20.dp), color = NammaWarmWhite.copy(0.05f))
                    ProfileMenuItem(Icons.Outlined.Payment, "Payment Methods", "Manage your saved cards and wallets") {}
                    Divider(modifier = Modifier.padding(horizontal = 20.dp), color = NammaWarmWhite.copy(0.05f))
                    ProfileMenuItem(Icons.Outlined.Settings, "Preferences", "App theme, notifications and language") {}
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, NammaError.copy(0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NammaError)
            ) {
                Icon(Icons.Default.ExitToApp, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Logout from App", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = NammaWarmWhite, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Text(label, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.labelSmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(NammaWarmWhite.copy(0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = NammaGold.copy(0.8f), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.bodySmall)
        }
        Icon(Icons.Default.ChevronRight, null, tint = NammaWarmWhite.copy(0.2f))
    }
}
