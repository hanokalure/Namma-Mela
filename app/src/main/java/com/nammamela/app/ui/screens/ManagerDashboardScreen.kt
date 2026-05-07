package com.nammamela.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.ManagerViewModel
import com.nammamela.app.viewmodel.PlayInsight
import com.nammamela.app.viewmodel.VENUE_SEAT_CAPACITY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen(
    viewModel: ManagerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val insights by viewModel.insights.collectAsState()
    val totalBookings by viewModel.totalBookings.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()

    Scaffold(
        containerColor = NammaDarkBrown,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Manager Insights", color = NammaGold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NammaDarkBrown)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "TOTAL BOOKINGS",
                        value = "$totalBookings",
                        icon = "🎟",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "REVENUE",
                        value = "₹$totalRevenue",
                        icon = "💰",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "PERFORMANCE BY PLAY",
                    color = NammaGold,
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                )
            }

            items(insights, key = { it.play.id }) { insight ->
                PlayInsightCard(insight)
            }

            item {
                Surface(
                    color = NammaGold.copy(0.05f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = NammaGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Bookings and revenue are calculated from tickets sold in this app. Seat utilization compares tickets sold to the fixed hall size ($VENUE_SEAT_CAPACITY seats).",
                            color = NammaWarmWhite.copy(0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayInsightCard(insight: PlayInsight) {
    val play = insight.play
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = play.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(color = NammaMaroon.copy(0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text(
                        text = "${play.rating} ★",
                        color = NammaGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "₹${insight.revenue.toInt()} revenue · ${insight.bookingCount} booking(s) · ${insight.ticketsSold} ticket(s)",
                color = NammaWarmWhite.copy(0.55f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Seat utilization (${insight.ticketsSold} / $VENUE_SEAT_CAPACITY)",
                color = NammaWarmWhite.copy(0.4f),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = insight.utilization,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = NammaMaroon,
                trackColor = Color.White.copy(0.08f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${(insight.utilization * 100).toInt()}% of hall capacity",
                color = NammaGold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: String, modifier: Modifier) {
    Surface(
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text(text = title, color = NammaGold.copy(0.6f), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}
