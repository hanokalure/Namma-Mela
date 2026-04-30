package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.theme.*

import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.viewmodel.AdminViewModel
import com.nammamela.app.viewmodel.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import com.nammamela.app.util.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToUploadPlay: () -> Unit,
    onNavigateToManageCast: (Int) -> Unit,
    onNavigateToManageSeats: (Int) -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateBack: () -> Unit,
    adminViewModel: AdminViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(LocalContext.current.findActivity()!!)
) {
    val activePlay by adminViewModel.activePlay.collectAsState()
    val bookedCount by adminViewModel.bookedCount.collectAsState()
    val revenue by adminViewModel.revenue.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val allPlays by adminViewModel.allPlays.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showPlaySelectionDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        adminViewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message = message)
        }
    }

    if (showPlaySelectionDialog) {
        AlertDialog(
            onDismissRequest = { showPlaySelectionDialog = false },
            title = { Text("Select Play to Manage Cast", color = NammaGold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                    if (allPlays.isEmpty()) {
                        Text("No plays found. Upload one first!", color = NammaWarmWhite.copy(0.5f))
                    }
                    allPlays.forEach { play ->
                        Surface(
                            onClick = { 
                                showPlaySelectionDialog = false
                                onNavigateToManageCast(play.id) 
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            color = NammaSurfaceLow,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(play.title, modifier = Modifier.padding(16.dp), color = NammaWarmWhite)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlaySelectionDialog = false }) {
                    Text("Close", color = NammaGold)
                }
            },
            containerColor = NammaSurfaceHigh
        )
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MANAGER'S CONSOLE", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        authViewModel.logout()
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.Logout, null, tint = NammaGold)
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
            // Welcome Card
            Surface(
                color = NammaMaroon,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.1f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = currentUser?.companyName ?: "Drama Company", 
                        color = NammaWarmWhite, 
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (activePlay != null) "Tonight's Play: ${activePlay?.title}" else "No active play tonight", 
                        color = NammaWarmWhite.copy(0.7f), 
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("MANAGEMENT TOOLS", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))

            AdminToolCard(
                title = "Upload Tonight's Play",
                subtitle = "Set poster, name, timing, and duration",
                icon = Icons.Default.AddPhotoAlternate,
                onClick = onNavigateToUploadPlay
            )
            Spacer(modifier = Modifier.height(16.dp))
            AdminToolCard(
                title = "Manage Tonight's Cast",
                subtitle = "Update Lead Actor, Comedian, Singer",
                icon = Icons.Default.Groups,
                onClick = { 
                    if (activePlay != null) {
                        onNavigateToManageCast(activePlay!!.id)
                    } else {
                        showPlaySelectionDialog = true
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AdminToolCard(
                title = "Performance Insights",
                subtitle = "View revenue and fan ratings",
                icon = Icons.Default.BarChart,
                onClick = onNavigateToInsights
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("QUICK STATS", color = NammaWarmWhite.copy(0.4f), style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("BOOKED", "$bookedCount/66", Modifier.weight(1f))
                StatCard("REVENUE", "₹$revenue", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(48.dp))
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
