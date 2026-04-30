package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.domain.model.Notification
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = NammaGold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NammaDarkBrown)
            )
        }
    ) { padding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(64.dp), tint = NammaGold.copy(0.2f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No new alerts yet", color = NammaWarmWhite.copy(0.5f))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onClick = { viewModel.markAsRead(notification.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (notification.isRead) NammaSurfaceLow else NammaSurfaceLow.copy(0.8f),
        shape = RoundedCornerShape(12.dp),
        border = if (notification.isRead) null else androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (notification.type) {
                            "BOOKING" -> Color(0xFF4CAF50).copy(0.2f)
                            "ALERT" -> Color(0xFFF44336).copy(0.2f)
                            else -> NammaGold.copy(0.2f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Notifications,
                    null,
                    tint = when (notification.type) {
                        "BOOKING" -> Color(0xFF4CAF50)
                        "ALERT" -> Color(0xFFF44336)
                        else -> NammaGold
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        color = if (notification.isRead) NammaWarmWhite else Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold
                        )
                    )
                    Text(
                        text = formatTimestamp(notification.timestamp),
                        color = NammaWarmWhite.copy(0.5f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    color = NammaWarmWhite.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
