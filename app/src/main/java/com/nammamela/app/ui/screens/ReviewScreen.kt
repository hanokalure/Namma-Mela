package com.nammamela.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammamela.app.ui.components.NammaMelaButton
import com.nammamela.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(playId: Int, onNavigateBack: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = NammaDeepTeal,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("THE FINAL ACT", color = NammaSaffron, style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = NammaSaffron)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NammaDeepTeal)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎭", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Rate the Spectacle",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "How was your experience watching Sati-Savitri?",
                color = Color.White.copy(0.6f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Star Rating
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 1..5) {
                    IconButton(onClick = { rating = i }) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = NammaSaffron,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
            
            Text(
                text = when(rating) {
                    1 -> "Poor Performance"
                    2 -> "Needs Improvement"
                    3 -> "Good Act"
                    4 -> "Brilliant Show"
                    5 -> "Masterpiece!"
                    else -> "Select a Star"
                },
                color = NammaSaffron,
                modifier = Modifier.padding(top = 16.dp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                placeholder = { Text("Write your review here...", color = Color.White.copy(0.2f)) },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = NammaSaffron,
                    unfocusedBorderColor = Color.White.copy(0.1f),
                    containerColor = NammaDarkTeal,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            NammaMelaButton(
                text = "SUBMIT REVIEW  🎭",
                onClick = onNavigateBack,
                enabled = rating > 0
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
