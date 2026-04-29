package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit
) {
    val notifications = listOf(
        NotificationData("Booking Success", "Your seats A1, A2 for SATI-SAVITRI have been confirmed.", Icons.Default.ConfirmationNumber, "2 mins ago"),
        NotificationData("New Play Added", "Bhakta Prahlada is now available for booking.", Icons.Default.Event, "1 hour ago"),
        NotificationData("Welcome to Namma-Mela", "Start exploring the heritage of Indian theater.", Icons.Default.Notifications, "1 day ago")
    )

    Scaffold(
        containerColor = NammaDarkBrown,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NOTIFICATIONS", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(notifications) { notification ->
                NotificationItem(notification)
            }
        }
    }
}

data class NotificationData(val title: String, val body: String, val icon: ImageVector, val time: String)

@Composable
fun NotificationItem(notification: NotificationData) {
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(NammaGold.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(notification.icon, null, tint = NammaGold, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(notification.title, color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text(notification.body, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(notification.time, color = NammaGold.copy(0.4f), fontSize = 10.sp)
            }
        }
    }
}
