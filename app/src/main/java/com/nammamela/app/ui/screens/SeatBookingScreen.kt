package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.domain.model.Seat
import com.nammamela.app.domain.model.SeatStatus
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.SeatBookingViewModel
import com.nammamela.app.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

// Trapezoidal shape for the seat headrest
val HeadrestShape = GenericShape { size, _ ->
    moveTo(size.width * 0.15f, 0f)
    lineTo(size.width * 0.85f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatBookingScreen(
    viewModel: SeatBookingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onBookingSuccess: (Int) -> Unit
) {
    val seats by viewModel.seats.collectAsState()
    val selectedSeats by viewModel.selectedSeats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var selectedTime by remember { mutableStateOf("06:30 PM") }
    val timeSlots = listOf("10:30 AM", "02:30 PM", "06:30 PM", "09:30 PM")

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.BookingSuccess -> {
                    onBookingSuccess(event.bookingId)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        containerColor = NammaDarkBrown,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CINEMA SEAT PLAN", color = NammaGold, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Curved Stage - Grey like image
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFC0C0C0), 
                    shape = RoundedCornerShape(bottomStart = 160.dp, bottomEnd = 160.dp),
                    modifier = Modifier.fillMaxWidth(0.9f).height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("STAGE", color = Color.White, style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 10.sp))
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Seat Grid with AGGRESSIVE Curvature
                if (isLoading && seats.isEmpty()) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NammaGold)
                    }
                } else {
                    val rowLabels = listOf("A", "B", "C", "D", "E", "F")
                    
                    Column(
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowLabels.forEach { rowLabel ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = rowLabel,
                                    color = NammaWarmWhite.copy(0.4f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(18.dp)
                                )
                                
                                val rowSeats = seats.filter { it.row == rowLabel }.sortedBy { it.column }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    rowSeats.forEachIndexed { index, seat ->
                                        // AISLES (2 | 7 | 2)
                                        if (index == 2 || index == 9) {
                                            Spacer(modifier = Modifier.width(20.dp))
                                        }
                                        
                                        // AGGRESSIVE Curve Offset
                                        val distanceFromCenter = index - 5f
                                        val curveOffset = (abs(distanceFromCenter) * abs(distanceFromCenter) * 2.5f).dp
                                        
                                        // Rotation for every seat in side blocks
                                        val rotation = when {
                                            index < 2 -> 35f // Deep wing angle
                                            index > 8 -> -35f
                                            else -> 0f
                                        }
                                        
                                        Box(modifier = Modifier.offset(y = curveOffset).rotate(rotation)) {
                                            SeatItem(
                                                seat = seat,
                                                onClick = { viewModel.toggleSeatSelection(seat) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Show Timings & Legend (Image Palette)
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(timeSlots) { time ->
                            val isTimeSelected = time == selectedTime
                            Surface(
                                onClick = { selectedTime = time },
                                color = if (isTimeSelected) Color(0xFF2196F3) else NammaSurfaceLow,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isTimeSelected) Color(0xFF2196F3) else NammaWarmWhite.copy(0.1f))
                            ) {
                                Text(
                                    text = time,
                                    color = if (isTimeSelected) Color.White else NammaWarmWhite.copy(0.5f),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendItem(color = Color(0xFF2196F3), text = "Available")
                        Spacer(modifier = Modifier.width(24.dp))
                        LegendItem(color = Color(0xFFDCDCDC), text = "Occupied")
                        Spacer(modifier = Modifier.width(24.dp))
                        LegendItem(color = NammaGold, text = "Selected")
                    }
                }
                
                if (selectedSeats.isNotEmpty()) {
                    NammaMelaButton(
                        text = "BOOK NOW - ₹${selectedSeats.size * 150}",
                        onClick = { viewModel.confirmReservation(userId = 1) },
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(2.dp)).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = NammaWarmWhite.copy(0.6f), style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun SeatItem(seat: Seat, onClick: () -> Unit) {
    val backgroundColor = when (seat.status) {
        SeatStatus.AVAILABLE -> Color(0xFF2196F3) 
        SeatStatus.SELECTED -> NammaGold
        SeatStatus.BOOKED -> Color(0xFFDCDCDC)
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(26.dp).clickable(onClick = onClick)
    ) {
        // Trapezoidal Headrest
        Box(
            modifier = Modifier.width(20.dp).height(5.dp).clip(HeadrestShape).background(backgroundColor.copy(0.7f))
        )
        // Cushion with subtle rounding
        Box(
            modifier = Modifier.fillMaxWidth().height(22.dp).clip(RoundedCornerShape(3.dp)).background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${seat.row}${seat.column}",
                color = if (seat.status == SeatStatus.SELECTED) NammaDarkBrown else Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 7.5.sp
            )
        }
    }
}
