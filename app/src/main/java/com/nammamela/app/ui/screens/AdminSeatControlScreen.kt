package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.model.SeatStatus
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.SeatBookingViewModel
import com.nammamela.app.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSeatControlScreen(
    viewModel: SeatBookingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val seats by viewModel.seats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            if (event is UiEvent.ShowSnackbar) {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("SEAT MAP CONTROL", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
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
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Instruction Card
            Surface(
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f)),
                modifier = Modifier.padding(vertical = 16.dp).padding(horizontal = 16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = NammaGold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Replicated Layout: 11 Seats (2-7-2) with Deep Arch.",
                        style = MaterialTheme.typography.bodySmall,
                        color = NammaWarmWhite.copy(0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Replicated Grid
            if (isLoading && seats.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NammaGold)
                }
            } else {
                val rowLabels = listOf("A", "B", "C", "D", "E", "F")
                
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowLabels.forEach { rowLabel ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = rowLabel,
                                color = NammaWarmWhite.copy(0.2f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(20.dp)
                            )
                            
                            val rowSeats = seats.filter { it.row == rowLabel }.sortedBy { it.column }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowSeats.forEachIndexed { index, seat ->
                                    if (index == 2 || index == 9) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                    
                                    val distanceFromCenter = abs(index - 5f)
                                    val curveOffset = (distanceFromCenter * distanceFromCenter * 1.5f).dp
                                    
                                    Box(modifier = Modifier.offset(y = curveOffset)) {
                                        AdminSeatItem(
                                            seat = seat,
                                            onClick = { viewModel.toggleSeatStatus(seat) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Stats
            Surface(
                modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth().padding(horizontal = 16.dp),
                color = NammaSurfaceLow,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NammaWarmWhite.copy(0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val availableCount = seats.count { it.status == SeatStatus.AVAILABLE }
                    val blockedCount = seats.count { it.status == SeatStatus.BOOKED }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("AVAILABLE", color = Color(0xFF2196F3), style = MaterialTheme.typography.labelSmall)
                        Text("$availableCount", color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("BLOCKED", color = Color(0xFFD3D3D3), style = MaterialTheme.typography.labelSmall)
                        Text("$blockedCount", color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL", color = NammaWarmWhite.copy(0.3f), style = MaterialTheme.typography.labelSmall)
                        Text("${seats.size}", color = NammaWarmWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSeatItem(seat: Seat, onClick: () -> Unit) {
    val backgroundColor = when (seat.status) {
        SeatStatus.AVAILABLE -> Color(0xFF2196F3)
        else -> Color(0xFFD3D3D3)
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(24.dp).clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.width(18.dp).height(4.dp)
                .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                .background(backgroundColor.copy(0.7f))
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(20.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${seat.column}",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp
            )
        }
    }
}
