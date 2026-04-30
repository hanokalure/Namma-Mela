package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen(
    viewModel: ManagerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val plays by viewModel.plays.collectAsState()
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
            // Summary Cards
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

            // Performance Header
            item {
                Text(
                    text = "DRAMA PERFORMANCE",
                    color = NammaGold,
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                )
            }

            // Play Stats List
            items(plays) { play ->
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
                            Text(text = play.title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Surface(color = NammaMaroon.copy(0.2f), shape = RoundedCornerShape(4.dp)) {
                                Text(text = "${play.rating} ★", color = NammaGold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Simulated Bar Chart
                        val progress = (play.rating / 5f).coerceIn(0.1f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(Color.White.copy(0.05f), RoundedCornerShape(4.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .fillMaxHeight()
                                    .background(NammaMaroon, RoundedCornerShape(4.dp))
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Fan Engagement", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall)
                            Text(text = "${(progress * 100).toInt()}%", color = NammaGold, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
            
            // Footer Info
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
                            text = "Data updates every night at 12:00 AM based on physical box office sync.",
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
