package com.nammamela.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*
import com.nammamela.app.viewmodel.TicketConfirmationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketConfirmationScreen(
    bookingId: Int,
    viewModel: TicketConfirmationViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val bookingWithPlay by viewModel.booking.collectAsState()
    
    val dateFormatter = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaDarkBrown)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bookingWithPlay == null) {
            CircularProgressIndicator(color = NammaGold)
        } else {
            val booking = bookingWithPlay!!.booking
            val play = bookingWithPlay!!.play
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "BOOKING CONFIRMED!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Digital Ticket
                Surface(
                    color = NammaSurfaceLow,
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NammaGold.copy(0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Top Section
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(play.title, color = NammaGold, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
                                Text("#${booking.id}", color = NammaGold.copy(0.3f), style = MaterialTheme.typography.labelSmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(play.genre, color = NammaWarmWhite.copy(0.5f), style = MaterialTheme.typography.bodySmall)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                TicketInfoItem("DATE", dateFormatter.format(Date(booking.timestamp)))
                                TicketInfoItem("TIME", "06:30 PM") // Defaulting time for now as not in entity
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                TicketInfoItem("SEATS", booking.seats)
                                TicketInfoItem("PRICE", "₹${booking.totalPrice.toInt()}")
                            }
                        }

                        // Perforated Line
                        DashedDivider()

                        // QR/Barcode Placeholder
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SCAN AT ENTRANCE", color = NammaGold.copy(0.3f), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 4.sp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                NammaMelaButton(
                    text = "BACK TO HOME",
                    onClick = onNavigateHome,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TicketInfoItem(label: String, value: String) {
    Column {
        Text(label, color = NammaGold.copy(0.5f), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp))
        Text(value, color = Color.White, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun DashedDivider() {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = NammaGold.copy(0.1f),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
        )
    }
}
