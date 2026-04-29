package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToUploadPlay: () -> Unit,
    onNavigateToManageCast: (Int) -> Unit,
    onNavigateToManageSeats: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = NammaDarkBrown,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MANAGER'S CONSOLE", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Welcome Card (Using NammaMaroon)
            Surface(
                color = NammaMaroon,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.1f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Gubbi Company Nataka", color = NammaWarmWhite, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                    Text("Tonight's Play: Sati-Savitri", color = NammaWarmWhite.copy(0.7f), style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("MANAGEMENT TOOLS", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))

            AdminToolCard(
                title = "Upload Tonight's Play",
                subtitle = "Set poster, name, and duration",
                icon = Icons.Default.AddPhotoAlternate,
                onClick = onNavigateToUploadPlay
            )
            Spacer(modifier = Modifier.height(16.dp))
            AdminToolCard(
                title = "Manage Tonight's Cast",
                subtitle = "Update Lead Actor, Comedian, Singer",
                icon = Icons.Default.Groups,
                onClick = { onNavigateToManageCast(1) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AdminToolCard(
                title = "Seat Map Control",
                subtitle = "Block/Reset seat availability",
                icon = Icons.Default.Chair,
                onClick = { onNavigateToManageSeats(1) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("QUICK STATS", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("BOOKED", "42/60", Modifier.weight(1f))
                StatCard("REVENUE", "₹6,300", Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminToolCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(NammaGold.copy(0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = NammaGold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ChevronRight, null, tint = NammaWarmWhite.copy(0.2f))
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = NammaSurfaceLow,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = NammaGold, style = MaterialTheme.typography.labelSmall)
            Text(value, color = NammaWarmWhite, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold))
        }
    }
}
